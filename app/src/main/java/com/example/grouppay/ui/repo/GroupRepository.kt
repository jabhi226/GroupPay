package com.example.grouppay.ui.repo

import com.example.grouppay.domain.GroupInfo
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroupList(): Flow<List<GroupInfo>>

    suspend fun saveNewGroup()

}