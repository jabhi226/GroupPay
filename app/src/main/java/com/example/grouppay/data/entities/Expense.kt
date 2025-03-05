package com.example.grouppay.data.entities

import com.example.grouppay.domain.entities.Expense as DomainExpense
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
    var totalAmountPaid: Double = 0.0
    var dateOfExpense: Long = System.currentTimeMillis()
    var remainingParticipants: RealmList<ExpenseMember> = realmListOf()
    var groupId: String = ""
    var isSquareOff: Boolean = false

    fun getDomainExpense(): DomainExpense {
        return DomainExpense(
            id = _id.toHexString(),
            label = label,
            paidBy = paidBy?.getDomainModel(),
            totalAmountPaid = totalAmountPaid,
            dateOfExpense = dateOfExpense,
            remainingParticipants = remainingParticipants.map { it.getDomainModel() },
            groupId = groupId
        )
    }

}