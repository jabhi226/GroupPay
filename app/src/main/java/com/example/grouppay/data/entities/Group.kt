package com.example.grouppay.data.entities

import com.example.grouppay.domain.Group as DomainGroup
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
    var participants: RealmList<GroupMember> = realmListOf()

    fun getDomainGroup(): DomainGroup {
        return DomainGroup(
            id = _id.toHexString(),
            name = name,
            expenses = expenses.toList().map { it.getDomainExpense() },
            participants = participants.map { it.getDomainModel() }
        )
    }
}
