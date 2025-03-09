package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.groups.utils.SquareOffUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
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


    val squareOffTransactions =
        MutableStateFlow<List<SquareOffTransactionModel>>(listOf())

    fun getSquareOffTransactions(groupId: String) {
        viewModelScope.launch {
            expensesRepository.getSquareOffTransactions(groupId).collectLatest {
                squareOffTransactions.emit(
                    SquareOffUtils.getSquareOffTransaction(it)
                )
            }
        }
    }

    fun squareOffTransaction(participant: SquareOffTransactionModel, groupId: String) {
        viewModelScope.launch {
            val isSaved = expensesRepository.upsertExpense(
                Expense(
                    label = "Square Off",
                    paidBy = getExpenseModelFromGroupModel(
                        participant.senderMember.copy(
                            amountOwedFromGroup = participant.amount
                        )
                    ),
                    totalAmountPaid = participant.amount,
                    remainingParticipants = listOf(
                        getExpenseModelFromGroupModel(
                            participant.receiverMember.copy(
                                amountBorrowedFromGroup = participant.amount
                            )
                        )
                    ),
                    groupId = groupId,
                    isSquareOff = true
                )
            )
            if (isSaved) {
                squareOffTransactions.emit(squareOffTransactions.value.filterNot { it == participant })
            }
        }
    }

    private fun getExpenseModelFromGroupModel(senderMember: GroupMember): ExpenseMember {
        return ExpenseMember(
            groupMemberId = senderMember.id,
            name = senderMember.name,
            amountOwedForExpense = senderMember.amountOwedFromGroup,
            amountBorrowedForExpense = senderMember.amountBorrowedFromGroup,
        )
    }

}