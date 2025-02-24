package com.example.grouppay.domain.repo

import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.domain.GroupWithTotalExpense
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

}