package com.example.grouppay.data.repo.impl

import android.util.Log
import com.example.grouppay.data.repo.GroupRepository
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.addExpense.model.ExpenseParticipant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
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

    override fun getGroupInformation(objectId: ObjectId): Group {
        return realm.query<Group>("_id=$0", objectId).find().first()
    }

    override fun getAllParticipantByText(text: String): Flow<List<Participant>> {
        return realm.query<Participant>("name CONTAINS $0", text).asFlow().map { it.list.toList() }
    }

    override suspend fun getAllParticipantByGroupId(groupId: String): ArrayList<ExpenseParticipant> {
        return withContext(Dispatchers.IO) {
            val group =
                realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
            group ?: return@withContext arrayListOf()
            println("====> group ${group.participants}")
            return@withContext ArrayList<ExpenseParticipant>().apply {
                group.participants.map {
                    ExpenseParticipant(participant = it)
                }
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
        try {
            Log.d("######", "saveNewParticipantInTheGroup: $groupId")
            return withContext(Dispatchers.IO) {
                realm.write {
                    val group =
                        realm.query<Group>("_id == $0", ObjectId(groupId)).find().firstOrNull()
                    group ?: return@write
                    Log.d("######", "participants: ${group.participants}")
                    group.participants.add(participant)
//                    val allParticipants = arrayListOf<Participant>().apply {
//                        addAll(group.participants)
//                    }
//                    allParticipants.add(participant)
//
//                    copyToRealm(
//                        Group().apply {
//                            name = group.name
//                            participants = allParticipants.toRealmList()
//                            expenses = group.expenses
//                            _id = group._id
//                        }, updatePolicy = UpdatePolicy.ALL
//                    )
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
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