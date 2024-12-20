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

    val allParticipantsByGroupId = MutableStateFlow<ArrayList<Participant>>(arrayListOf())
    val paidBy = MutableStateFlow<Participant?>(null)

    fun getParticipantsByGroupId(groupId: String?) {
        viewModelScope.launch {
            groupId ?: return@launch
            allParticipantsByGroupId.emit(repository.getAllParticipantByGroupId(groupId))
        }
    }

    fun updateParticipant(participant: Participant) {
        viewModelScope.launch {
            allParticipantsByGroupId.emit(
                ArrayList<Participant>().apply {
                    allParticipantsByGroupId.value.map {
                        if (it._id == participant._id) {
                            participant
                        } else {
                            it
                        }
                    }
                }
            )
        }
    }

    fun updatePaidBy(participant: Participant) {
        viewModelScope.launch {
            paidBy.emit(participant)
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