package com.example.grouppay.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.repo.GroupRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupList = repository.getGroupList()

    val saveResponse: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun saveNewGroup(groupName: String) {
        viewModelScope.launch {
            repository.saveNewGroup(groupName)
            saveResponse.emit(true)
        }
    }

    fun saveParticipateWithGroup() {

    }

    fun getGroupInformation(_id: ObjectId) = repository.getGroupInformation(_id)

    fun getAllParticipantsByText(text: String) = repository.getAllParticipantByText(text)

    fun saveNewParticipantInTheGroup(groupId: String, participant: Participant) {
        viewModelScope.launch {
            repository.saveNewParticipantInTheGroup(groupId, participant)
            saveResponse.emit(true)
        }
    }

}