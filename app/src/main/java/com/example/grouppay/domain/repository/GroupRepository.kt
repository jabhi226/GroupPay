package com.example.grouppay.domain.repository

import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.entities.GroupWithTotalExpense
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroupList(): Flow<List<GroupWithTotalExpense>>

    suspend fun saveNewGroup(group: String)

    suspend fun getGroupInformation(objectId: String): Flow<Group>

    fun getAllParticipantByText(text: String): Flow<List<GroupMember>>

    suspend fun getAllParticipantByGroupId(groupId: String): ArrayList<ExpenseMember>

    fun getAllParticipantByGroupIdFlow(groupId: String): Flow<ArrayList<ExpenseMember>>

    suspend fun saveNewParticipantInTheGroup(groupId: String, participant: GroupMember): ExpenseMember?

    suspend fun upsertExpense(expense: Expense): Boolean

    suspend fun getExpensesByGroupId(groupId: String): List<Expense>

    suspend fun deleteGroupMember(groupMemberId: String, groupId: String): Boolean

    suspend fun getParticipantDetails(participantId: String?, groupId: String?): Flow<GroupMember?>

}