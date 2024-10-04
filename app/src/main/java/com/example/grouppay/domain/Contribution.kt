package com.example.grouppay.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Contribution : RealmObject {

    @PrimaryKey
    val _id: ObjectId = ObjectId()
    var paidBy: Splitter? = null
    val remainingSplitters: RealmList<Splitter> = realmListOf()
    var group: GroupInfo? = null

}