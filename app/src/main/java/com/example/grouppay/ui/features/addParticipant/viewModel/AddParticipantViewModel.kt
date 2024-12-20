package com.example.grouppay.ui.features.addParticipant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Participant
import com.example.grouppay.data.repo.GroupRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddParticipantViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val saveResponse: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun getAllParticipantsByText(text: String) = repository.getAllParticipantByText(text)

    fun saveNewParticipantInTheGroup(groupId: String, participant: Participant) {
        viewModelScope.launch {
            saveResponse.emit(
                repository.saveNewParticipantInTheGroup(groupId, participant)
            )
        }
    }

}