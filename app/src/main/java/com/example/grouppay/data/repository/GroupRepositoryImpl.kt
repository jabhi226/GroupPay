package com.example.grouppay.data.repository

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.domain.entities.PendingPayments
import com.example.grouppay.domain.entities.GroupMember as DomainGroupMember
import com.example.grouppay.domain.entities.Group as DomainGroup
import com.example.grouppay.domain.entities.Expense as DomainExpense
import com.example.grouppay.domain.repository.GroupRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class GroupRepositoryImpl(
    private val realm: Realm
) : GroupRepository {
    override fun getGroupListWithExpenses(): Flow<List<Pair<DomainGroup, List<DomainExpense>>>> = flow {
        val groupDetails = arrayListOf<Pair<DomainGroup, List<DomainExpense>>>()
        val groups = realm
            .query<Group>()
            .find()

        groups.forEach {
            val expenses =
                realm.query<Expense>("groupId == $0", it._id.toHexString()).find()
            groupDetails.add(Pair(it.getDomainGroup(), expenses.map { it.getDomainExpense() }))
        }
        emit(groupDetails)
    }.flowOn(Dispatchers.IO)

    override suspend fun getGroupInformation(objectId: String): Flow<DomainGroup> = flow {
        val participants = ArrayList<DomainGroupMember>()
        val grp = realm.query<Group>("_id=$0", ObjectId(objectId)).find().first()
        participants.addAll(grp.participants.map { it.getDomainModel() })
        val expenses = realm.query<Expense>("groupId = $0", grp._id.toHexString()).find()

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
                            amountBorrowed
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
    }.flowOn(Dispatchers.IO)

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