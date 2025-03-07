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
        expenses.forEach { expense ->
            println("==> ${expense.isSquareOff}")
            val paidBy = expense.paidBy ?: return@forEach
            for (i in participants.indices) {
                val p = participants[i]
                if (p.id == paidBy.groupMemberId) {
                    if (!expense.isSquareOff) {
                        p.amountOwedFromGroup = paidBy.amountOwedForExpense
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
                    val managedParticipant =
                        copyToRealm(participant.getDataModel(), updatePolicy = UpdatePolicy.ALL)
                    val latestGroup = findLatest(group) ?: return@write null
                    var isMemberExist = false
                    for (index in latestGroup.participants.indices) {
                        println("==> ${latestGroup.participants[index]._id.toHexString()} | ${managedParticipant._id.toHexString()} | ${latestGroup.participants[index]._id == managedParticipant._id}")
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
        return realm.query<Expense>("groupId == $0", groupId).find().reversed().map { it.getDomainExpense() }
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
        participantId: String?,
        groupId: String?
    ): Flow<DomainGroupMember?> = flow {
        val groupMembers = realm.query<GroupMember>(
            "_id=$0",
            ObjectId(participantId ?: return@flow)
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
                amountOwedFromGroup = amountOwed.roundToTwoDecimal(),
                amountBorrowedFromGroup = amountBorrowed.roundToTwoDecimal(),
                amountReturnedToOwner = amountReturned.roundToTwoDecimal(),
                amountReceivedFromBorrower = amountReceived.roundToTwoDecimal()
            )
        )
    }
}