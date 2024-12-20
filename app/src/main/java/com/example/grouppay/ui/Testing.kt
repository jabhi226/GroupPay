package com.example.grouppay.ui

import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.ObjectId

object Testing {


    //todo @Abhi remove this as it used for testing
    fun getContros(): RealmList<Expense> {
        return (0 until 0).map {
            Expense().apply {
                paidBy = groupMembers()[0]
                remainingParticipants = groupMembers().drop(1).toRealmList()
            }
        }.toRealmList()
    }

    fun groupMembers(): RealmList<Participant> {
        return (0 until 0).map {
            getParticipent()
        }.toRealmList()
    }

    fun getParticipent(): Participant {
        return Participant().apply {
            name = "Test"
            amountOwedFromGroup = 0.0
        }
    }

    fun groupWithTotalExpense(): GroupWithTotalExpense {
        return GroupWithTotalExpense(
            ObjectId(),
            "Karjat trip",
            3,
            100.0
        )
    }

    fun getGroupWithTotalExpenses(): List<GroupWithTotalExpense> {
        return (0 until 0).map {
            groupWithTotalExpense()
        }
    }

    fun getExpense(): Expense {
        return Expense().apply {
            label = "Cake Cutting"
            paidBy = getParticipent()
            remainingParticipants = groupMembers()
        }
    }

    fun getExpenses(): RealmList<Expense> {
        return (0 until 0).map {
            getExpense()
        }.toRealmList()
    }

}