package com.example.grouppay.ui.repo

import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface GroupRepository {

    fun getGroupList(): Flow<List<GroupWithTotalExpense>>

    suspend fun saveNewGroup(group: String)

    fun getGroupInformation(_id: ObjectId): Group

    fun getAllParticipantByText(text: String): Flow<List<Participant>>

    suspend fun saveNewParticipantInTheGroup(groupId: String, participant: Participant)

}