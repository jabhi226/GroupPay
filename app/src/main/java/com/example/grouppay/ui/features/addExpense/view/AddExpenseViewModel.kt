package com.example.grouppay.ui.features.addExpense.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.domain.repo.GroupRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val allParticipantsByGroupId = MutableStateFlow<List<ExpenseMember>>(listOf())
    val paidBy = MutableStateFlow(ExpenseMember(groupMemberId = "", name = ""))
    private var groupId: String? = null

    private val _uiEvents = Channel<UiEvents>()
    val uiEvents get() = _uiEvents.receiveAsFlow()

    fun getParticipantsByGroupId(groupId: String?) {
        viewModelScope.launch {
            this@AddExpenseViewModel.groupId = groupId
            groupId ?: return@launch
            val list = repository.getAllParticipantByGroupId(groupId)
            allParticipantsByGroupId.emit(list)
        }
    }

    fun updateParticipantAmount(participant: ExpenseMember, totalAmountPaid: String) {
        viewModelScope.launch {
            var total = 0.0
            allParticipantsByGroupId.emit(allParticipantsByGroupId.value.map {
                if (it.id == participant.groupMemberId) {
                    total += participant.amountBorrowedForExpense
                    participant
                } else {
                    total += it.amountBorrowedForExpense
                    it
                }
            })
//            if (total != (totalAmountPaid.toDoubleOrNull() ?: 0.0)) {
//                _uiEvents.send(UiEvents.ShowError("Total amount mismatch."))
//            }
        }
    }

    fun updateParticipantSelection(participantId: String, totalAmountPaid: String) {
        var currentSelectedParticipants = allParticipantsByGroupId.value.count { it.isSelected }
        if (allParticipantsByGroupId.value.find { it.groupMemberId == participantId }?.isSelected == true) {
            currentSelectedParticipants--
        } else {
            currentSelectedParticipants++
        }
        allParticipantsByGroupId.value = allParticipantsByGroupId.value.map {
            val item = it.copy(
                isSelected = if (it.groupMemberId == participantId) {
                    !it.isSelected
                } else {
                    it.isSelected
                }
            )
            item.copy(
                amountBorrowedForExpense = if (item.isSelected) {
                    (totalAmountPaid.toDoubleOrNull() ?: 0.0) / currentSelectedParticipants
                } else {
                    0.0
                }
            )
        }
    }

    fun updatePaidBy(participant: ExpenseMember) {
        viewModelScope.launch {
            paidBy.emit(participant)
        }
    }

    fun saveExpense(totalAmountPaid: String, expenseName: String) {
        viewModelScope.launch {
            val gId = groupId
            if (gId == null) {
                _uiEvents.send(UiEvents.ShowError("Group Id not found"))
                return@launch
            } else {
                groupId
            }
            if (expenseName.isEmpty()) {
                _uiEvents.send(UiEvents.ShowError("Please give a name for the expense"))
                return@launch
            }
            if (allParticipantsByGroupId.value.count { it.isSelected } <= 0) {
                _uiEvents.send(UiEvents.ShowError("Select at lease one member for the split"))
                return@launch
            }
            val response = repository.upsertExpense(
                Expense(
                    label = expenseName,
                    paidBy = paidBy.value.copy(
                        amountOwedForExpense = (totalAmountPaid.toDoubleOrNull() ?: 0.0)
                    ),
                    totalAmountPaid = allParticipantsByGroupId.value.sumOf { it.amountBorrowedForExpense },
                    remainingParticipants = allParticipantsByGroupId.value,
                    groupId = gId
                )
            )
            _uiEvents.send(
                if (response) {
                    UiEvents.NavigateUp
                } else {
                    UiEvents.ShowError("Error adding Expense ${expenseName}.")
                }
            )
        }
    }

    sealed class UiEvents {
        data class ShowError(val error: String) : UiEvents()
        data object NavigateUp : UiEvents()
    }

}