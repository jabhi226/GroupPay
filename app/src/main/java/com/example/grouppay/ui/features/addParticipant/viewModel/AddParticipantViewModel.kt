package com.example.grouppay.ui.features.addParticipant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.domain.GroupMember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddParticipantViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupMember: MutableStateFlow<GroupMember?> = MutableStateFlow(null)
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
            val response = repository.saveNewParticipantInTheGroup(groupId, participant)
            if (response != null) {
                uiEvents.emit(UiEvents.ShowSuccess)
            } else {
                uiEvents.emit(UiEvents.ShowError("Error adding group member ${participant.name}."))
            }
        }
    }

    fun getParticipantDetails(participantId: String?) {
        viewModelScope.launch {
            if (participantId == null) return@launch
            repository.getParticipantDetails(participantId)?.also {
                groupMember.emit(it)
            }
        }
    }

    fun updateGroupMemberName(name: String) {
        groupMember.update {
            it?.copy(name = name)
        }
    }

    sealed class UiEvents() {
        data class ShowError(val errorMessage: String) : UiEvents()
        data object ShowSuccess : UiEvents()
    }

}