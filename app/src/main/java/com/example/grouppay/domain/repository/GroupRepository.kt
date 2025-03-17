package com.example.grouppay.domain.repository

import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.domain.entities.GroupWithTotalExpense
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroupList(): Flow<List<GroupWithTotalExpense>>

    suspend fun saveNewGroup(group: String)

    suspend fun getGroupInformation(objectId: String): Flow<Group>

}