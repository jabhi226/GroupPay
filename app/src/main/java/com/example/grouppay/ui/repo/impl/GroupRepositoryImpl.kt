package com.example.grouppay.ui.repo.impl

import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.example.grouppay.ui.repo.GroupRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
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

    override fun getGroupInformation(_id: ObjectId): Group {
        return realm.query<Group>("_id=$0", _id).find().first()
    }

    override fun getAllParticipantByText(text: String): Flow<List<Participant>> {
        return realm.query<Participant>("name CONTAINS $0", text).asFlow().map { it.list.toList() }
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

    override suspend fun saveNewParticipantInTheGroup(groupId: String, participant: Participant) {
        withContext(Dispatchers.IO) {
            realm.write {
                val group = realm.query<Group>("_id=$0", groupId).find().first()
                group.participants.add(participant) // Add to the list
            }
        }
    }

    private fun getSplitters(): RealmList<Participant> {
        return listOf("Abhishek", "Suraj").map {
            Participant().apply {
                name = it
            }
        }.toRealmList()
    }
}