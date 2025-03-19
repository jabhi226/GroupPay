package com.example.grouppay.data.repository

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.repository.MembersRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId

class MemberRepositoryImpl(
    private val realm: Realm
) : MembersRepository {
    override suspend fun saveNewMemberInTheGroup(
        groupId: String,
        participant: GroupMember
    ): ExpenseMember? {
        return withContext(Dispatchers.IO) {
            realm.write {
                try {
                    val group =
                        realm.query<Group>("_id == $0", BsonObjectId(groupId)).find().firstOrNull()
                            ?: return@write null
                    val managedParticipant =
                        copyToRealm(participant.getDataModel(), updatePolicy = UpdatePolicy.ALL)
                    val latestGroup = findLatest(group) ?: return@write null
                    var isMemberExist = false
                    for (index in latestGroup.participants.indices) {
                        if (latestGroup.participants[index]._id == managedParticipant._id) {
                            latestGroup.participants[index] = managedParticipant
                            isMemberExist = true
                            break
                        }
                    }
                    if (!isMemberExist) {
                        latestGroup.participants.add(managedParticipant)
                    }
                    return@write managedParticipant.getExpenseMemberModel()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@write null
                }
            }
        }
    }

    override fun getMemberDetails(participantId: String?, groupId: String?): Flow<GroupMember?> =
        flow {
            val groupMembers = realm.query<com.example.grouppay.data.entities.GroupMember>(
                "_id=$0",
                BsonObjectId(participantId ?: return@flow)
            ).find()
            if (groupMembers.isEmpty()) {
                return@flow
            }

            val groupMember = groupMembers.first().getDomainModel()
            val expenses = realm.query<Expense>("groupId = $0", groupId).find()
            var amountOwed = 0.0
            var amountBorrowed = 0.0
            var amountReturned = 0.0
            var amountReceived = 0.0
            expenses.forEach { expense ->
                val paidBy = expense.paidBy ?: return@forEach
                if (groupMember.id == paidBy.groupMemberId) {
                    if (expense.isSquareOff) {
                        amountReturned += paidBy.amountOwedForExpense
                    } else {
                        amountOwed += paidBy.amountOwedForExpense
                        for (it in expense.remainingParticipants) {
                            if (it.groupMemberId == groupMember.id) {
                                amountBorrowed += it.amountBorrowedForExpense
                                break
                            }
                        }
                    }
                } else {
                    val paidTo = expense.remainingParticipants.firstOrNull {
                        it.groupMemberId == groupMember.id
                    }
                    paidTo?.let {
                        if (expense.isSquareOff) {
                            amountReceived += expense.totalAmountPaid
                        } else {
                            amountBorrowed += it.amountBorrowedForExpense
                        }
                    }
                }
            }
            emit(
                groupMember.copy(
                    amountOwedFromGroup = amountOwed,
                    amountBorrowedFromGroup = amountBorrowed,
                    amountReturnedToOwner = amountReturned,
                    amountReceivedFromBorrower = amountReceived
                )
            )
        }.flowOn(Dispatchers.IO)

    override fun getAllMembersByGroupId(groupId: String): Flow<ArrayList<ExpenseMember>> = flow {
        val group =
            realm.query<Group>("_id == $0", BsonObjectId(groupId)).find().firstOrNull()?.asFlow()
        group?.collect { realmResults ->
            emit(
                ArrayList<ExpenseMember>().apply {
                    addAll(realmResults.obj?.participants?.map { it.getExpenseMemberModel() }
                        ?: listOf())
                }
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteGroupMember(groupMemberId: String, groupId: String): Boolean {
        return withContext(Dispatchers.IO) {
            realm.write {
                try {
                    val group =
                        realm.query<Group>("_id == $0", org.mongodb.kbson.BsonObjectId(groupId))
                            .find().firstOrNull()
                            ?: return@write false
                    val latestGroup = findLatest(group) ?: return@write false
                    val isRemoved =
                        latestGroup.participants.removeIf { it._id.toHexString() == groupMemberId }
                    return@write isRemoved
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@write false
                }
            }
        }
    }

    override fun getAllParticipantByText(text: String): Flow<List<GroupMember>> {
        return realm.query<com.example.grouppay.data.entities.GroupMember>("name CONTAINS $0", text)
            .asFlow()
            .map { realmList ->
                realmList.list.map {
                    it.getDomainModel()
                }
            }
            .flowOn(Dispatchers.IO)
    }
}