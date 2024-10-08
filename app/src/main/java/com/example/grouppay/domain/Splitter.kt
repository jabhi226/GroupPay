package com.example.grouppay.domain

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Splitter : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userName: String = ""
    var amountBorrowed: Double = 0.0
    var amountOwed: Double = 0.0
}