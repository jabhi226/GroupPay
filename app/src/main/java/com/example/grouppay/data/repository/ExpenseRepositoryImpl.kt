package com.example.grouppay.data.repository

import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.entities.PendingPayments
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import com.google.gson.Gson
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
                        println("====> existingExpense.totalAmountPaid ${existingExpense.totalAmountPaid}")
                    } else {
                        expense.remainingParticipants.forEach {
                            copyToRealm(it.getDataModel(), updatePolicy = UpdatePolicy.ALL)
                        }
                        println(">> new expense ${expense}")
                        println(">> new expense ${expense}")
                        val dbExpense =
                            copyToRealm(
                                instance = expense.copy(totalAmountPaid = expense.remainingParticipants.sumOf { it.amountBorrowedForExpense }).getDataModel(),
                                updatePolicy = UpdatePolicy.ALL
                            )
                        println("====> dbExpense.totalAmountPaid ${dbExpense.totalAmountPaid}")
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

    override fun getSquareOffTransactions(objectId: String): Flow<ArrayList<GroupMember>> = flow {
        val participants = ArrayList<GroupMember>()
        val grp = realm.query<Group>("_id=$0", BsonObjectId(objectId)).find().first()
        participants.addAll(grp.participants.map { it.getDomainModel() })
        val expenses = realm.query<com.example.grouppay.data.entities.Expense>(
            "groupId = $0",
            grp._id.toHexString()
        ).find()
        println(">>" + Gson().toJson(participants))
        println(">>" + Gson().toJson(expenses.map { it.getDomainExpense() }))
        expenses.forEach { expense ->
            val paidBy = expense.paidBy ?: return@forEach
            for (i in participants.indices) {
                val p = participants[i]
                println("=-=-> $p")
                if (p.id == paidBy.groupMemberId) {
                    println("---> amountOwedForExpense ${paidBy.amountOwedForExpense}")
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
                        println("---> amountBorrowedForExpense ${it.amountBorrowedForExpense}")
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
                            amountBorrowed.roundToTwoDecimal()
                        )
                    )
                }

                participants[i] = p
            }
        }
        emit(participants)
    }
}