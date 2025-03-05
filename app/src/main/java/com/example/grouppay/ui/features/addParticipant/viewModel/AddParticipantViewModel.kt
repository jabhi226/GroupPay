package com.example.grouppay.ui.features.addParticipant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.domain.entities.GroupMember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddParticipantViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupMember: MutableStateFlow<GroupMember> = MutableStateFlow(GroupMember(name = ""))
    val uiEvents: MutableSharedFlow<UiEvents?> = MutableSharedFlow()

    fun saveNewParticipantInTheGroup(groupId: String?, participant: GroupMember?) {
        viewModelScope.launch {
            if (groupId == null) {
                uiEvents.emit(UiEvents.ShowError("Group not found"))
                return@launch
            }
            if (participant == null || participant.name.isEmpty()) {
                uiEvents.emit(UiEvents.ShowError("Please enter group member name"))
                return@launch
            }
            println("==> ${participant.name} | ${groupMember.value.name} | ${participant.profilePictureUriPath} | ${groupMember.value.profilePictureUriPath}")
            val response = repository.saveNewParticipantInTheGroup(groupId, participant)
            if (response != null) {
                uiEvents.emit(UiEvents.ShowSuccess)
            } else {
                uiEvents.emit(UiEvents.ShowError("Error adding group member ${participant.name}."))
            }
        }
    }

    fun getParticipantDetails(participantId: String?, groupId: String?) {
        viewModelScope.launch {
            if (participantId == null) return@launch
            repository.getParticipantDetails(participantId, groupId).collectLatest { grp ->
                grp?.let {
                    groupMember.emit(it)
                }
            }
        }
    }

    fun updateGroupMemberName(name: String) {
        groupMember.update {
            it.copy(name = name)
        }
    }

    fun updateGroupMemberProfileUri(uri: String) {
        groupMember.update {
            it.copy(profilePictureUriPath = uri)
        }
    }

    sealed class UiEvents() {
        data class ShowError(val errorMessage: String) : UiEvents()
        data object ShowSuccess : UiEvents()
    }

}