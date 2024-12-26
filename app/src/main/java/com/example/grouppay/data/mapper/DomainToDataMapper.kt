package com.example.grouppay.data.mapper

import com.example.grouppay.domain.Participant as DomainParticipant
import com.example.grouppay.data.entities.Participant
import org.mongodb.kbson.ObjectId


fun DomainParticipant.getDataModel(): Participant {
    return Participant().apply {
        if (id.isNotEmpty()) _id = ObjectId(id)
        name = this@getDataModel.name
        amountBorrowedFromGroup = this@getDataModel.amountBorrowedFromGroup
        amountOwedFromGroup = this@getDataModel.amountOwedFromGroup
    }
}