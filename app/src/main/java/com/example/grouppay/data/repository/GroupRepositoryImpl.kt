package com.example.grouppay.data.repository

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.GroupMember
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.entities.PendingPayments
import com.example.grouppay.domain.entities.ExpenseMember as DomainExpenseMember
import com.example.grouppay.domain.entities.Expense as DomainExpense
import com.example.grouppay.domain.entities.GroupMember as DomainGroupMember
import com.example.grouppay.domain.entities.Group as DomainGroup
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.domain.entities.GroupWithTotalExpense
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import com.google.gson.Gson
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId.Companion.invoke
import org.mongodb.kbson.ObjectId

class GroupRepositoryImpl(
    private val realm: Realm
) : GroupRepository {
    override fun getGroupList(): Flow<List<GroupWithTotalExpense>> {
        return realm
            .query<Group>()
            .asFlow()
            .map { results ->
                results.list.map { group ->
                    val expense =
                        realm.query<Expense>("groupId == $0", group._id.toHexString()).find()
                    GroupWithTotalExpense(
                        group._id.toHexString(),
                        group.name,
                        group.participants.size,
                        expense.sumOf { expense1 ->
                            expense1.remainingParticipants.sumOf {
                                it.amountBorrowedForExpense
                            }
                        }.roundToTwoDecimal()
                    )
                }
            }
    }

    override suspend fun getGroupInformation(objectId: String): Flow<DomainGroup> = flow {
        val participants = ArrayList<DomainGroupMember>()
        val grp = realm.query<Group>("_id=$0", ObjectId(objectId)).find().first()
        participants.addAll(grp.participants.map { it.getDomainModel() })
        val expenses = realm.query<Expense>("groupId = $0", grp._id.toHexString()).find()

        println(">> getGroupInformation  ${Gson().toJson(participants)}")
        println(">> getGroupInformation  ${Gson().toJson(expenses.map { it.getDomainExpense() })}")
        println(">> getGroupInformation  ${Gson().toJson(expenses.reversed().map { it.getDomainExpense() })}")

        expenses.forEach { expense ->
            println("==> ${expense.isSquareOff}")
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
                        println("==> ${p.name} | ${p.amountBorrowedFromGroup} | ${it.amountBorrowedForExpense}")
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
        emit(
            DomainGroup(
                id = grp._id.toHexString(),
                name = grp.name,
                participants = participants
            )
        )
    }

    override suspend fun saveNewGroup(group: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(Group().apply {
                    name = group
                }, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

}