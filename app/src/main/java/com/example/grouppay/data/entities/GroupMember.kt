package com.example.grouppay.data.entities

import com.example.grouppay.domain.GroupMember as DomainParticipant
import com.example.grouppay.domain.ExpenseMember as DomainExpenseMember
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class GroupMember : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var amountBorrowedFromGroup: Double = 0.0
    var amountOwedFromGroup: Double = 0.0
    var profilePictureUriPath: String? = null

    fun setAmountBorrowedFromGroup(rsText: String) {
        try {
            amountBorrowedFromGroup = rsText.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDomainModel(): DomainParticipant {
        return DomainParticipant(
            id = _id.toHexString(),
            name = name,
            amountBorrowedFromGroup = amountBorrowedFromGroup,
            amountOwedFromGroup = amountOwedFromGroup,
            profilePictureUriPath = profilePictureUriPath
        )
    }

    fun getExpenseMemberModel(): DomainExpenseMember {
        return DomainExpenseMember(
            groupMemberId = _id.toHexString(),
            name = name
        )
    }
}