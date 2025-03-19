package com.example.grouppay.data.repository

import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.entities.PendingPayments
import com.example.grouppay.domain.entities.SquareOffTransactionModel
import com.example.grouppay.domain.repository.ExpenseRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId

class ExpenseRepositoryImpl(
    private val realm: Realm
) : ExpenseRepository {

    override suspend fun upsertExpense(expense: Expense): Boolean {
        try {
            return withContext(Dispatchers.IO) {
                return@withContext realm.write {
                    if (expense.id.isNotEmpty()) {
                        val existingExpense =
                            realm.query<com.example.grouppay.data.entities.Expense>(
                                "_id = $0",
                                BsonObjectId(expense.id)
                            ).find().first()
                        existingExpense.label = expense.label
                        existingExpense.paidBy = expense.paidBy?.getDataModel()
                        existingExpense.totalAmountPaid =
                            expense.remainingParticipants.sumOf { it.amountBorrowedForExpense }
                        existingExpense.dateOfExpense = expense.dateOfExpense
                        existingExpense.remainingParticipants =
                            expense.remainingParticipants.map { it.getDataModel() }.toRealmList()
                        existingExpense.groupId = expense.groupId
                    } else {
                        expense.remainingParticipants.forEach {
                            copyToRealm(it.getDataModel(), updatePolicy = UpdatePolicy.ALL)
                        }
                        val dbExpense =
                            copyToRealm(
                                instance = expense.copy(totalAmountPaid = expense.remainingParticipants.sumOf { it.amountBorrowedForExpense })
                                    .getDataModel(),
                                updatePolicy = UpdatePolicy.ALL
                            )
                        dbExpense.remainingParticipants.forEach {
                            it.groupExpenseId = dbExpense._id.toHexString()
                        }
                    }
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun getExpensesByGroupId(groupId: String): List<Expense> {
        return realm.query<com.example.grouppay.data.entities.Expense>("groupId == $0", groupId)
            .find()
            .reversed()
            .map { it.getDomainExpense() }
    }

    override fun getSquareOffTransactions(objectId: String): Flow<List<SquareOffTransactionModel>> =
        flow {
            val participants = ArrayList<GroupMember>()
            val grp = realm.query<Group>("_id=$0", BsonObjectId(objectId)).find().first()
            participants.addAll(grp.participants.map { it.getDomainModel() })
            val expenses = realm.query<com.example.grouppay.data.entities.Expense>(
                "groupId = $0",
                grp._id.toHexString()
            ).find()
            expenses.forEach { expense ->
                val paidBy = expense.paidBy ?: return@forEach
                for (i in participants.indices) {
                    val p = participants[i]
                    if (p.id == paidBy.groupMemberId) {
                        if (!expense.isSquareOff) {
                            p.amountOwedFromGroup += paidBy.amountOwedForExpense
                        } else {
                            p.amountReturnedToOwner = paidBy.amountOwedForExpense
                        }
                    } else {
                        var amountBorrowed = 0.0
                        val participant =
                            expense.remainingParticipants.find { it.groupMemberId == p.id }
                        participant?.let {
                            amountBorrowed = it.amountBorrowedForExpense
                            if (!expense.isSquareOff) {
                                p.amountBorrowedFromGroup += amountBorrowed
                            } else {
                                p.amountReceivedFromBorrower = amountBorrowed
                            }
                        }
                        p.pendingPaymentsMapping.add(
                            PendingPayments(
                                paidBy.groupMemberId,
                                paidBy.name,
                                amountBorrowed
                            )
                        )
                    }

                    participants[i] = p
                }
            }
            val transactions = getSquareOffTransaction(participants)
            emit(transactions)
        }.flowOn(Dispatchers.IO)

    private fun getSquareOffTransaction(groupMembers: List<GroupMember>): List<SquareOffTransactionModel> {
        val squreOffValues = mutableMapOf<GroupMember, Double>()
        val toBePaidValues = mutableMapOf<GroupMember, Double>()
        val toBeReceivedValues = mutableMapOf<GroupMember, Double>()

        groupMembers.map { groupMember ->
            toBePaidValues[groupMember] = groupMember.getAmountToBePaid()
        }

        toBePaidValues.forEach { groupMember ->
            val amountToReceive =
                groupMembers
                    .filterNot { it.id == groupMember.key.id }
                    .sumOf { member ->
                        member.pendingPaymentsMapping
                            .filter { it.originalPayerId == groupMember.key.id }
                            .sumOf { it.amountToBePaid }
                    }
            toBeReceivedValues[groupMember.key] = amountToReceive
            squreOffValues[groupMember.key] =
                amountToReceive - (toBePaidValues[groupMember.key] ?: 0.0)
        }

        return getMinimumTransactions(squreOffValues)
    }

    private fun getMinimumTransactions(squreOffValues: MutableMap<GroupMember, Double>): List<SquareOffTransactionModel> {
        // Split participants into Debtors and Creditors
        val creditors = mutableListOf<Pair<GroupMember, Double>>()  // List of (id, amount owed)
        val debtors = mutableListOf<Pair<GroupMember, Double>>()  // List of (id, amount to pay)

        squreOffValues.forEach { (groupMember, balance) ->
            if (balance > 0) {
                creditors.add(groupMember to balance) // They need to receive money
            } else if (balance < 0) {
                debtors.add(groupMember to -balance) // They need to pay money (negative balance, we store positive amount here)
            }
        }

        // Greedily match Debtors with Creditors
        val transactions =
            mutableListOf<SquareOffTransactionModel>()  // List of transactions (from, to, amount)

        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
            val creditor = creditors.first()
            val debtor = debtors.first()

            val paymentAmount = minOf(creditor.second, debtor.second)

            // Record the transaction (debtor pays creditor)
            transactions.add(SquareOffTransactionModel(debtor.first, creditor.first, paymentAmount))

            // Update balances after payment
            if (creditor.second > paymentAmount) {
                creditors[0] = creditor.copy(second = creditor.second - paymentAmount)
            } else {
                creditors.removeAt(0)  // Credit is fully settled
            }

            if (debtor.second > paymentAmount) {
                debtors[0] = debtor.copy(second = debtor.second - paymentAmount)
            } else {
                debtors.removeAt(0)  // Debt is fully settled
            }
        }
        return transactions
    }

}