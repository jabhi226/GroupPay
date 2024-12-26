package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.domain.GroupMember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupList = repository.getGroupList()

    val expenses = MutableStateFlow<List<Expense>>(listOf())

    fun getExpensesByGroupId(groupId: String) {
        viewModelScope.launch {
            expenses.emit(repository.getExpensesByGroupId(groupId))
        }
    }

    val saveResponse: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun saveParticipateWithGroup() {

    }

    fun getGroupInformation(objectId: String) = repository.getGroupInformation(objectId)

    fun getAllParticipantsByText(text: String) = repository.getAllParticipantByText(text)

    fun saveNewParticipantInTheGroup(groupId: String, participant: GroupMember) {
//        viewModelScope.launch {
//            repository.saveNewParticipantInTheGroup(groupId, participant)
//            saveResponse.emit(true)
//        }
    }

}