package com.example.grouppay.data.entities

import com.example.grouppay.domain.entities.Group as DomainGroup
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Group : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var participants: RealmList<GroupMember> = realmListOf()

    fun getDomainGroup(): DomainGroup {
        return DomainGroup(
            id = _id.toHexString(),
            name = name,
            participants = participants.map { it.getDomainModel() }
        )
    }
}
