package com.example.grouppay.data.repo

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.GroupMember
import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.ExpenseMember as DomainExpenseMember
import com.example.grouppay.domain.Expense as DomainExpense
import com.example.grouppay.domain.GroupMember as DomainParticipant
import com.example.grouppay.domain.Group as DomainGroup
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                results.list.map {
                    GroupWithTotalExpense(
                        it._id.toHexString(),
                        it.name,
                        it.participants.size,
                        it.expenses.sumOf { contribution ->
                            contribution.paidBy?.amountOwedForExpense ?: 0.0
                        }
                    )
                }
            }
    }

    override fun getGroupInformation(objectId: String): DomainGroup {
        return realm.query<Group>("_id=$0", ObjectId(objectId)).find().first().getDomainGroup()
    }

    override fun getAllParticipantByText(text: String): Flow<List<DomainParticipant>> {
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
        participant: DomainParticipant
    ): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext realm.write {
                try {
                    val group =
                        realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
                    group ?: return@write false
                    val managedParticipant = copyToRealm(participant.getDataModel())
                    val latestGroup = findLatest(group) ?: return@write false
                    return@write latestGroup.participants.add(managedParticipant)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@write false
                }
            }
        }
    }

    override suspend fun upsertExpense(expense: DomainExpense): Boolean {
        println("=======> $expense")
        try {
            return withContext(Dispatchers.IO) {
                return@withContext realm.write {
                    if (expense.id.isNotEmpty()) {
//                        val group = realm.query<Group>("_id = $0", ObjectId(expense.id)).find().first()
                        val existingExpense =
                            realm.query<Expense>("_id = $0", ObjectId(expense.id)).find().first()
                        existingExpense.label = expense.label
                        existingExpense.paidBy = expense.paidBy?.getDataModel()
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
//        return realm.query<Expense>("groupId == $0", groupId).find().map { it.getDomainExpense() }
        val l = realm.query<Expense>("groupId == $0", groupId).find().map { it.getDomainExpense() }
        println("=======> $l")
        return l
    }
}