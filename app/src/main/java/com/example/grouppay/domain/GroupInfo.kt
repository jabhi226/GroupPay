package com.example.grouppay.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class GroupInfo : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var groupName: String = ""
    var contributions: RealmList<Contribution> = realmListOf()
    var groupMembers: RealmList<Splitter> = realmListOf()
}
