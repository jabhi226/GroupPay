package com.example.grouppay.ui.features.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupList = repository.getGroupList()

    val expenses = MutableStateFlow<List<Expense>>(listOf())
    fun getExpensesByGroupId(groupId: String) {
        viewModelScope.launch {
            expenses.emit(repository.getExpensesByGroupId(groupId))
        }
    }

    val groupInfo = MutableStateFlow<Group?>(null)
    fun getGroupInformation(objectId: String) {
        viewModelScope.launch {
            groupInfo.emit(repository.getGroupInformation(objectId))
        }
    }

    val groupInfoFlow = MutableStateFlow<Group?>(null)
    fun getGroupInformationFlow(objectId: String) {
        viewModelScope.launch {
            repository.getGroupInformationFlow(objectId).collectLatest {
                groupInfoFlow.emit(it)
            }
        }
    }

    val squareOffTransactions =
        MutableStateFlow<List<SquareOffTransactionModel>>(listOf())

    fun getSquareOffTransactions(groupId: String) {
        viewModelScope.launch {
            squareOffTransactions.emit(
                SquareOffUtils.getSquareOffTransaction(
                    repository.getGroupInformation(
                        groupId
                    )
                )
            )
        }
    }

    fun squareOffTransaction(participant: SquareOffTransactionModel, groupId: String) {
        viewModelScope.launch {
            val isSaved = repository.upsertExpense(
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
                    groupId = groupId
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