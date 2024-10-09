package com.example.grouppay.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class Expense : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var label: String = ""
    var paidBy: Participant? = null
    @Ignore
    var dateOfExpense: Date? = Date()
    var remainingParticipants: RealmList<Participant> = realmListOf()
    var group: Group? = null

}