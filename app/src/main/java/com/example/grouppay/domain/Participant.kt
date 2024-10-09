package com.example.grouppay.domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Participant : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var amountBorrowedFromGroup: Double = 0.0
    var amountOwedFromGroup: Double = 0.0
}