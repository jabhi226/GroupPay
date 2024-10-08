package com.example.grouppay.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Contribution : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var paidBy: Splitter? = null
    var remainingSplitters: RealmList<Splitter> = realmListOf()
    var group: GroupInfo? = null

}