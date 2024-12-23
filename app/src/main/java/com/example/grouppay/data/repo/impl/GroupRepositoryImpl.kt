package com.example.grouppay.data.repo.impl

import com.example.grouppay.data.repo.GroupRepository
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
                        it._id,
                        it.name,
                        it.participants.size,
                        it.expenses.sumOf { contribution ->
                            contribution.paidBy?.amountOwedFromGroup ?: 0.0
                        }
                    )
                }
            }
    }

    override fun getGroupInformation(objectId: ObjectId): Group {
        return realm.query<Group>("_id=$0", objectId).find().first()
    }

    override fun getAllParticipantByText(text: String): Flow<List<Participant>> {
        return realm.query<Participant>("name CONTAINS $0", text).asFlow().map { it.list.toList() }
    }

    override suspend fun getAllParticipantByGroupId(groupId: String): ArrayList<Participant> {
        return withContext(Dispatchers.IO) {
            val group =
                realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
            group ?: return@withContext arrayListOf()
            return@withContext ArrayList<Participant>().apply {
                addAll(group.participants)
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
        participant: Participant
    ): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext realm.write {
                try {
                    val group =
                        realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
                    group ?: return@write false
                    val managedParticipant = copyToRealm(participant)
                    val latestGroup = findLatest(group) ?: return@write false
                    return@write latestGroup.participants.add(managedParticipant)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@write false
                }
            }
        }
    }
}