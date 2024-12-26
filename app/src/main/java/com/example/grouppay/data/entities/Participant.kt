package com.example.grouppay.data.entities

import com.example.grouppay.domain.Participant as DomainParticipant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Participant : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var amountBorrowedFromGroup: Double = 0.0
    var amountOwedFromGroup: Double = 10.0

    fun setAmountBorrowedFromGroup(rsText: String) {
        try {
            amountBorrowedFromGroup = rsText.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDomainModel(): DomainParticipant {
        return DomainParticipant(
            id = _id.toHexString(),
            name = name,
            amountBorrowedFromGroup = amountBorrowedFromGroup,
            amountOwedFromGroup = amountOwedFromGroup
        )
    }
}