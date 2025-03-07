package com.example.grouppay.ui

import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.entities.GroupWithTotalExpense

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
            name = "Test TestTes tTes tTe stTest Test ",
            amountOwedFromGroup = 34.3,
            amountBorrowedFromGroup = 123.2,
            amountReturnedToOwner = 0.0,
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
            label = "Square Off",
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