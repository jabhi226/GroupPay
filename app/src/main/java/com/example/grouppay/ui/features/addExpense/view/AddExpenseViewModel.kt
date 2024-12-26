package com.example.grouppay.ui.features.addExpense.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.domain.Participant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val allParticipantsByGroupId = MutableStateFlow<List<Participant>>(listOf())
    val paidBy = MutableStateFlow<Participant?>(null)
    val totalAmountPaid = MutableStateFlow("0")

    fun getParticipantsByGroupId(groupId: String?) {
        viewModelScope.launch {
            groupId ?: return@launch
            val list = repository.getAllParticipantByGroupId(groupId)
            allParticipantsByGroupId.emit(list)
        }
    }

    fun updateParticipantAmount(participant: Participant) {
        viewModelScope.launch {
            val list = ArrayList<Participant>().apply {
                addAll(
                    allParticipantsByGroupId.value.map {
                        if (it.id == participant.id) {
                            participant
                        } else {
                            it
                        }
                    })
            }
            allParticipantsByGroupId.emit(list)
        }
    }

    fun updateParticipantSelection(participantId: String) {
        var currentSelectedParticipants = allParticipantsByGroupId.value.count { it.isSelected }
        if (allParticipantsByGroupId.value.find { it.id == participantId }?.isSelected == true) {
            currentSelectedParticipants--
        } else {
            currentSelectedParticipants++
        }
        allParticipantsByGroupId.value = allParticipantsByGroupId.value.map {
            val item = it.copy(
                isSelected = if (it.id == participantId) {
                    !it.isSelected
                } else {
                    it.isSelected
                }
            )
            item.copy(
                amountBorrowedFromGroup = if (item.isSelected) {
                    (totalAmountPaid.value.toDoubleOrNull() ?: 0.0) / currentSelectedParticipants
                } else {
                    0.0
                }
            )
        }
    }

    fun updatePaidBy(participant: Participant) {
        viewModelScope.launch {
            paidBy.emit(participant)
        }
    }

    fun saveExpense() {

    }

    fun updateTotalAmountPaid(amountText: String) {
        viewModelScope.launch {
            totalAmountPaid.emit(
                if (amountText.endsWith(".")) {
                    amountText.replace(".", "")
                } else {
                    amountText
                }
            )
        }
    }
}