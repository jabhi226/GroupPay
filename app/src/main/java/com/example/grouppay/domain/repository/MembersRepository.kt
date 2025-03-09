package com.example.grouppay.domain.repository

import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.GroupMember
import kotlinx.coroutines.flow.Flow

interface MembersRepository {

    suspend fun saveNewMemberInTheGroup(
        groupId: String,
        participant: GroupMember
    ): ExpenseMember?

    fun getMemberDetails(
        participantId: String?,
        groupId: String?
    ): Flow<GroupMember?>

    fun getAllMembersByGroupId(
        groupId: String
    ): Flow<ArrayList<ExpenseMember>>

    suspend fun deleteGroupMember(
        groupMemberId: String,
        groupId: String
    ): Boolean

    fun getAllParticipantByText(
        text: String
    ): Flow<List<GroupMember>>
}