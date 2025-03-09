package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.groups.utils.SquareOffUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository,
    private val expensesRepository: ExpenseRepository,
) : ViewModel() {

    val groupList = repository.getGroupList()

    val groupInfo = MutableStateFlow<Group?>(null)
    fun getGroupInformation(objectId: String) {
        viewModelScope.launch {
            repository.getGroupInformation(objectId).collectLatest {
                groupInfo.emit(it)
            }
        }
    }

}