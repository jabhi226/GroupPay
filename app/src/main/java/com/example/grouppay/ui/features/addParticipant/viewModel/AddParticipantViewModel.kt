package com.example.grouppay.ui.features.addParticipant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.ui.features.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddParticipantViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val saveResponse: MutableStateFlow<UiEvents?> = MutableStateFlow(null)

    fun saveNewParticipantInTheGroup(groupId: String?, participant: GroupMember) {
        viewModelScope.launch {
            if (groupId == null) {
                saveResponse.emit(UiEvents.ShowError("Group not found"))
                return@launch
            }
            if (participant.name.isEmpty()) {
                saveResponse.emit(UiEvents.ShowError("Please enter group member name"))
                return@launch
            }
            val response = repository.saveNewParticipantInTheGroup(groupId, participant)
            if (response) {
                saveResponse.emit(UiEvents.ShowSuccess)
            } else {
                saveResponse.emit(UiEvents.ShowError("Error adding group member ${participant.name}."))
            }
        }
    }

    sealed class UiEvents() {
        data class ShowError(val errorMessage: String) : UiEvents()
        data object ShowSuccess : UiEvents()
    }

}