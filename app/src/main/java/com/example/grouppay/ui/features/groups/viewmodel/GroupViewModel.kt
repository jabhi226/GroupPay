package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
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

    val groupInfo = MutableStateFlow<Group?>(null)
    fun getGroupInformation(objectId: String) {
        viewModelScope.launch {
            groupInfo.emit(repository.getGroupInformation(objectId))
        }
    }

    fun getSquareOffTransactions(group: Group) {
        viewModelScope.launch {

        }
    }

}