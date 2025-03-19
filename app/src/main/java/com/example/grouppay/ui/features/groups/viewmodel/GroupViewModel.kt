package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.ui.features.groups.view.screens.AllGroupsScreenUiState
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository,
    private val expensesRepository: ExpenseRepository,
) : ViewModel() {


    val groupInfo = MutableStateFlow<Group?>(null)
    fun getGroupInformation(objectId: String) {
        viewModelScope.launch {
            repository.getGroupInformation(objectId).collectLatest {
                groupInfo.emit(it)
            }
        }
    }

    val groupListUiState = MutableStateFlow<AllGroupsScreenUiState>(AllGroupsScreenUiState.Loading)
    fun getGroupList() {
        viewModelScope.launch {
            repository.getGroupListWithExpenses().collectLatest { groupWithExpenses ->
                groupListUiState.emit(
                    if (groupWithExpenses.isEmpty()) {
                        AllGroupsScreenUiState.Error("")
                    } else {
                        AllGroupsScreenUiState.GroupList(groupWithExpenses.map { it ->
                            val group = it.first
                            val expense = it.second
                            GroupWithTotalExpense(
                                group.id,
                                group.name,
                                group.participants.size,
                                expense.sumOf { expense1 ->
                                    expense1.remainingParticipants.sumOf {
                                        it.amountBorrowedForExpense
                                    }
                                }.roundToTwoDecimal()
                            )
                        })
                    }
                )
            }
        }
    }

}