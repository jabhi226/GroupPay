package com.example.grouppay.ui

import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense

object Testing {

    //todo @Abhi remove this as it used for testing
    fun getContros(): List<Expense> {
        return (0..0).map {
            Expense(
                paidBy = groupExpenseMembers()[0],
                remainingParticipants = groupExpenseMembers(),
                id = "",
                label = "test",
                dateOfExpense = System.currentTimeMillis(),
                groupId = ""
            )
        }
    }

    fun groupMembers(): List<GroupMember> {
        return (0..0).map {
            getParticipent()
        }
    }

    fun groupExpenseMembers(): List<ExpenseMember> {
        return (0..2).map {
            ExpenseMember(
                id = it.toString(),
                groupMemberId = "",
                name = "",
                isSelected = if (it == 10) false else true
            )
        }
    }

    fun getParticipent(): GroupMember {
        return GroupMember(
            name = "Test",
            amountOwedFromGroup = 0.0,
            id = ""
        )
    }

    fun groupWithTotalExpense(): GroupWithTotalExpense {
        return GroupWithTotalExpense(
            "",
            "Karjat trip",
            3,
            100.0
        )
    }

    fun getGroupWithTotalExpenses(): List<GroupWithTotalExpense> {
        return (0..0).map {
            groupWithTotalExpense()
        }
    }

    fun getExpense(): Expense {
        return Expense(
            label = "Lunch",
            paidBy = groupExpenseMembers()[0],
            id = "",
            dateOfExpense = System.currentTimeMillis(),
            remainingParticipants = groupExpenseMembers(),
            groupId = ""
        )
    }

    fun getExpenses(): List<Expense> {
        return (0 until 0).map {
            getExpense()
        }
    }

}