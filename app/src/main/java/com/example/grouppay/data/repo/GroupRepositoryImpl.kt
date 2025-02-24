package com.example.grouppay.data.repo

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.GroupMember
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.PendingPayments
import com.example.grouppay.domain.ExpenseMember as DomainExpenseMember
import com.example.grouppay.domain.Expense as DomainExpense
import com.example.grouppay.domain.GroupMember as DomainGroupMember
import com.example.grouppay.domain.Group as DomainGroup
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.domain.GroupWithTotalExpense
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
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
                        }
                    )
                }
            }
    }

    override suspend fun getGroupInformation(objectId: String): Flow<DomainGroup> = flow {
        val participants = ArrayList<DomainGroupMember>()
//        val payers = hashMapOf<String, ArrayList<Pair<String, Double>>>()// payerId, [{toBePaid, Amount},{toBePaid, Amount}]
        val grp = realm.query<Group>("_id=$0", ObjectId(objectId)).find().first()
        participants.addAll(grp.participants.map { it.getDomainModel() })
//        payers.putAll(grp.participants.map { Pair(it._id.toHexString(), arrayListOf()) })
        val expenses = realm.query<Expense>("groupId = $0", grp._id.toHexString()).find()
        expenses.forEach { expense ->
            val paidBy = expense.paidBy ?: return@forEach
            for (i in participants.indices) {
                val p = participants[i]
                if (p.id == paidBy.groupMemberId) {
                    val amountOwed = paidBy.amountOwedForExpense
                    println("==> amountOwed: ${paidBy.amountOwedForExpense} | $amountOwed")
                    p.amountOwedFromGroup += amountOwed
                } else {
                    val participant =
                        expense.remainingParticipants.find { it.groupMemberId == p.id } ?: continue
                    val amountBorrowed = participant.amountBorrowedForExpense
                    println("==> amountBorrowed: ${p.amountBorrowedFromGroup} | $amountBorrowed")
                    p.amountBorrowedFromGroup += amountBorrowed
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

    override fun getAllParticipantByText(text: String): Flow<List<DomainGroupMember>> {
        return realm.query<GroupMember>("name CONTAINS $0", text).asFlow().map { realmList ->
            realmList.list.map {
                it.getDomainModel()
            }
        }
    }

    override suspend fun getAllParticipantByGroupId(groupId: String): ArrayList<DomainExpenseMember> {
        return withContext(Dispatchers.IO) {
            val group =
                realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
            group ?: return@withContext arrayListOf()
            return@withContext ArrayList<DomainExpenseMember>().apply {
                addAll(group.participants.map { it.getExpenseMemberModel() })
            }
        }
    }

    override fun getAllParticipantByGroupIdFlow(groupId: String): Flow<ArrayList<DomainExpenseMember>> =
        flow {
            val group =
                realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()?.asFlow()
            group?.collect { realmResults ->
                emit(
                    ArrayList<DomainExpenseMember>().apply {
                        addAll(realmResults.obj?.participants?.map { it.getExpenseMemberModel() }
                            ?: listOf())
                    }
                )
            }
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

    override suspend fun saveNewParticipantInTheGroup(
        groupId: String,
        participant: DomainGroupMember
    ): DomainExpenseMember? {
        return withContext(Dispatchers.IO) {
            realm.write {
                try {
                    val group =
                        realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
                            ?: return@write null
                    val managedParticipant = copyToRealm(participant.getDataModel())
                    val latestGroup = findLatest(group) ?: return@write null
                    latestGroup.participants.add(managedParticipant)
                    return@write managedParticipant.getExpenseMemberModel()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@write null
                }
            }
        }
    }


    override suspend fun upsertExpense(expense: DomainExpense): Boolean {
        try {
            return withContext(Dispatchers.IO) {
                return@withContext realm.write {
                    if (expense.id.isNotEmpty()) {
//                        val group = realm.query<Group>("_id = $0", ObjectId(expense.id)).find().first()
                        val existingExpense =
                            realm.query<Expense>("_id = $0", ObjectId(expense.id)).find().first()
                        existingExpense.label = expense.label
                        existingExpense.paidBy = expense.paidBy?.getDataModel()
                        existingExpense.totalAmountPaid = expense.totalAmountPaid
                        existingExpense.dateOfExpense = expense.dateOfExpense
                        existingExpense.remainingParticipants =
                            expense.remainingParticipants.map { it.getDataModel() }.toRealmList()
                        existingExpense.groupId = expense.groupId
                    } else {
                        expense.remainingParticipants.forEach {
                            copyToRealm(it.getDataModel(), updatePolicy = UpdatePolicy.ALL)
                        }
                        val dbExpense =
                            copyToRealm(expense.getDataModel(), updatePolicy = UpdatePolicy.ALL)
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

    override suspend fun getExpensesByGroupId(groupId: String): List<DomainExpense> {
        return realm.query<Expense>("groupId == $0", groupId).find().map { it.getDomainExpense() }
    }

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

    override suspend fun getParticipantDetails(
        participantId: String?
    ): DomainGroupMember? {
        println("===> $participantId")
        val groupMembers = realm.query<GroupMember>(
            "_id=$0",
            ObjectId(participantId ?: return null)
        )
            .find()
        if (groupMembers.isEmpty()) {
            return null
        }
        return groupMembers.first().getDomainModel()
    }
}