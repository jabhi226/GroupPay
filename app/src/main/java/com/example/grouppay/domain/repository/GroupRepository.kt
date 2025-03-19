package com.example.grouppay.domain.repository

import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroupListWithExpenses(): Flow<List<Pair<Group, List<Expense>>>>

    suspend fun saveNewGroup(group: String)

    suspend fun getGroupInformation(objectId: String): Flow<Group>

}