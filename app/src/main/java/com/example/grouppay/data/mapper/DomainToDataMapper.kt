package com.example.grouppay.data.mapper

import com.example.grouppay.domain.Participant as DomainParticipant
import com.example.grouppay.data.entities.Participant
import com.example.grouppay.domain.Expense as DomainExpense
import com.example.grouppay.data.entities.Expense
import com.example.grouppay.domain.Group as DomainGroup
import com.example.grouppay.data.entities.Group
import io.realm.kotlin.ext.toRealmList
import org.mongodb.kbson.ObjectId


fun DomainParticipant.getDataModel(): Participant {
    return Participant().apply {
        _id = if (id.isNotEmpty()) { ObjectId(id) } else { ObjectId() }
        name = this@getDataModel.name
        amountBorrowedFromGroup = this@getDataModel.amountBorrowedFromGroup
        amountOwedFromGroup = this@getDataModel.amountOwedFromGroup
    }
}

fun DomainExpense.getDataModel(): Expense {
    return Expense().apply {
        _id = if (id.isNotEmpty()) { ObjectId(id) } else { ObjectId() }
        label = this@getDataModel.label
        paidBy = this@getDataModel.paidBy?.getDataModel()
        dateOfExpense = this@getDataModel.dateOfExpense
        remainingParticipants =
            this@getDataModel.remainingParticipants.map { it.getDataModel() }.toRealmList()
    }
}

fun DomainGroup.getDataModel(): Group {
    return Group().apply {
        _id = if (id.isNotEmpty()) { ObjectId(id) } else { ObjectId() }
        name = this@getDataModel.name
        participants = this@getDataModel.participants.map { it.getDataModel() }.toRealmList()
    }
}