package com.example.grouppay.ui.features.addExpense.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.data.repo.GroupRepository
import com.example.grouppay.domain.Participant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val allParticipantsByGroupId = MutableStateFlow<ArrayList<Participant>>(ArrayList())
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
                        if (it._id == participant._id) {
                            participant
                        } else {
                            it
                        }
                    })
            }
            allParticipantsByGroupId.emit(list)
        }
    }

    fun updateParticipantSelection(participant: Participant) {
        viewModelScope.launch {
            val list = ArrayList<Participant>().apply {
                addAll(
                    allParticipantsByGroupId.value.map {
                        if (it._id == participant._id) {
                            participant.apply {
                                isSelected = !isSelected
                            }
                        } else {
                            it
                        }
                    })
            }
            allParticipantsByGroupId.emit(list)
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