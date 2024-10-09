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

    override suspend fun saveNewGroup(group: Group) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(group, updatePolicy = UpdatePolicy.ALL)
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