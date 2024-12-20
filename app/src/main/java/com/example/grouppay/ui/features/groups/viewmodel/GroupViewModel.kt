package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Participant
import com.example.grouppay.data.repo.GroupRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupList = repository.getGroupList()

    val saveResponse: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun saveParticipateWithGroup() {

    }

    fun getGroupInformation(objectId: ObjectId) = repository.getGroupInformation(objectId)

    fun getAllParticipantsByText(text: String) = repository.getAllParticipantByText(text)

    fun saveNewParticipantInTheGroup(groupId: String, participant: Participant) {
//        viewModelScope.launch {
//            repository.saveNewParticipantInTheGroup(groupId, participant)
//            saveResponse.emit(true)
//        }
    }

}