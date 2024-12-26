package com.example.grouppay.data.entities

import com.example.grouppay.data.mapper.getDataModel
import com.example.grouppay.domain.Expense as DomainExpense
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Expense : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var label: String = ""
    var paidBy: ExpenseMember? = null
    var dateOfExpense: Long = System.currentTimeMillis()
    var remainingParticipants: RealmList<ExpenseMember> = realmListOf()
    var groupId: String = ""

    fun getDomainExpense(): DomainExpense {
        return DomainExpense(
            id = _id.toHexString(),
            label = label,
            paidBy = paidBy?.getDomainModel(),
            dateOfExpense = dateOfExpense,
            remainingParticipants = remainingParticipants.map { it.getDomainModel() },
            groupId = groupId
        )
    }

}