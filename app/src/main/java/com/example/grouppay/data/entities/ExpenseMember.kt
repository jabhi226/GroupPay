package com.example.grouppay.data.entities

import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ExpenseMember : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var groupExpenseId: String = ""
    var groupMemberId: String = ""
    var name: String = ""
    var amountBorrowedForExpense: Double = 0.0
    var amountOwedForExpense: Double = 0.0

    fun getDomainModel(): ExpenseMember {
        return ExpenseMember(
            id = _id.toHexString(),
            groupExpenseId = groupExpenseId,
            groupMemberId = groupMemberId,
            name = name,
            amountBorrowedForExpense = amountBorrowedForExpense.roundToTwoDecimal(),
            amountOwedForExpense = amountOwedForExpense.roundToTwoDecimal()
        )
    }

}