package com.example.grouppay.ui.features.addExpense.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.data.repo.GroupRepository
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.addExpense.model.ExpenseParticipant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val allParticipantsByGroupId = MutableStateFlow<ArrayList<ExpenseParticipant>>(arrayListOf())
    val paidBy = MutableStateFlow<Participant?>(null)

    fun getParticipantsByGroupId(groupId: String?) {
        viewModelScope.launch {
            groupId ?: return@launch
            val list = repository.getAllParticipantByGroupId(groupId)
            println("====> list $list")
            allParticipantsByGroupId.emit(list)
        }
    }

    fun updateParticipant(participant: ExpenseParticipant) {
        viewModelScope.launch {
            allParticipantsByGroupId.emit(
                ArrayList<ExpenseParticipant>().apply {
                    allParticipantsByGroupId.value.map {
                        if (it.participant?._id == participant.participant?._id) {
                            participant
                        } else {
                            it
                        }
                    }
                }
            )
        }
    }

    fun updatePaidBy(participant: ExpenseParticipant) {
        viewModelScope.launch {
            paidBy.emit(participant.participant)
        }
    }

//    val remainingParticipants = MutableStateFlow<ArrayList<Participant>>(arrayListOf())
//    fun addParticipant(participant: Participant) {
//        viewModelScope.launch {
//            if (remainingParticipants.value.find { it._id == participant._id } != null) {
//                // participant is already added
//                return@launch
//            }
//            remainingParticipants.emit(
//                remainingParticipants.value.apply {
//                    add(participant)
//                })
//        }
//    }

    fun saveExpense() {

    }
}