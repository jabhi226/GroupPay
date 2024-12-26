package com.example.grouppay.ui

import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense

object Testing {

    //todo @Abhi remove this as it used for testing
    fun getContros(): List<Expense> {
        return (0..0).map {
            Expense(
                paidBy = groupMembers()[0],
                remainingParticipants = groupMembers().drop(1),
                id = "",
                label = "test",
                dateOfExpense = System.currentTimeMillis(),
                groupId = ""
            )
        }
    }

    fun groupMembers(): List<Participant> {
        return (0..0).map {
            getParticipent()
        }
    }

    fun getParticipent(): Participant {
        return Participant(
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
            label = "Cake Cutting",
            paidBy = getParticipent(),
            id = "",
            dateOfExpense = System.currentTimeMillis(),
            remainingParticipants = groupMembers(),
            groupId = ""
        )
    }

    fun getExpenses(): List<Expense> {
        return (0 until 0).map {
            getExpense()
        }
    }

}