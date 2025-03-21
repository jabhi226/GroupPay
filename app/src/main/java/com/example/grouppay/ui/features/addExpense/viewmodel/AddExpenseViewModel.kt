package com.example.grouppay.ui.features.addExpense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.domain.repository.MembersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val membersRepository: MembersRepository
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
            membersRepository.getAllMembersByGroupId(groupId).collect {
                allParticipantsByGroupId.emit(it)
            }
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

    fun saveExpense(expenseName: String) {
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
            val totalAmountPaid = allParticipantsByGroupId.value.sumOf { it.amountBorrowedForExpense }
            val response = expenseRepository.upsertExpense(
                Expense(
                    label = expenseName,
                    paidBy = paidBy.value.copy(
                        amountOwedForExpense = totalAmountPaid
                    ),
                    totalAmountPaid = totalAmountPaid,
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

    fun saveNewMember(groupId: String?, memberName: String) {
        viewModelScope.launch {
            if (groupId == null) {
                _uiEvents.send(UiEvents.ShowError("Can not found group."))
                return@launch
            }
            val savedParticipant =
                membersRepository.saveNewMemberInTheGroup(groupId, GroupMember(name = memberName))
            if (savedParticipant == null) {
                _uiEvents.send(UiEvents.ShowError("Can not found group."))
                return@launch
            }
            updatePaidBy(savedParticipant)
        }
    }

    sealed class UiEvents {
        data class ShowError(val error: String) : UiEvents()
        data object NavigateUp : UiEvents()
    }

}