package com.example.grouppay.ui.repo.impl

import com.example.grouppay.domain.Contribution
import com.example.grouppay.domain.GroupInfo
import com.example.grouppay.domain.Splitter
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

class GroupRepositoryImpl(
    private val realm: Realm
) : GroupRepository {
    override fun getGroupList(): Flow<List<GroupInfo>> {
        return realm
            .query<GroupInfo>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }

    override suspend fun saveNewGroup() {
        withContext(Dispatchers.IO) {
            realm.write {
                val group = GroupInfo().apply {
                    groupName = "Office group test"
                    groupMembers = getSplitters()
                }
                copyToRealm(group, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    private fun getSplitters(): RealmList<Splitter> {
        return listOf("Abhishek", "Suraj").map {
            Splitter().apply {
                userName = it
            }
        }.toRealmList()
    }
}