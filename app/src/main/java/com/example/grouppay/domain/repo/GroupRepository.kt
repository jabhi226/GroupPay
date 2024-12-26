package com.example.grouppay.domain.repo

import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroupList(): Flow<List<GroupWithTotalExpense>>

    suspend fun saveNewGroup(group: String)

    fun getGroupInformation(objectId: String): Group

    fun getAllParticipantByText(text: String): Flow<List<Participant>>

    suspend fun getAllParticipantByGroupId(groupId: String): ArrayList<Participant>

    suspend fun saveNewParticipantInTheGroup(groupId: String, participant: Participant): Boolean

}