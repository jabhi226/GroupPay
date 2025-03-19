package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val expensesRepository: ExpenseRepository
) : ViewModel() {

    val expenses = MutableStateFlow<List<Expense>>(listOf())
    fun getExpensesByGroupId(groupId: String) {
        viewModelScope.launch {
            expenses.emit(expensesRepository.getExpensesByGroupId(groupId))
        }
    }

}