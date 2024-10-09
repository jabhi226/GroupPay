package com.example.grouppay.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Group : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var expenses: RealmList<Expense> = realmListOf()
    var participants: RealmList<Participant> = realmListOf()
}
