package com.example.grouppay.domain.entities

import com.example.grouppay.ui.features.utils.roundToTwoDecimal

data class GroupMember(
    var id: String = "",
    var name: String,
    var amountBorrowedFromGroup: Double = 0.0,
    var amountOwedFromGroup: Double = 0.0,
    var amountReturnedToOwner: Double = 0.0,
    var amountReceivedFromBorrower: Double = 0.0,
    val pendingPaymentsMapping: ArrayList<PendingPayments> = arrayListOf(),
    var profilePictureUriPath: String? = null
) {
    fun setAmountBorrowedFromGroup(rsText: String) {
        try {
            amountBorrowedFromGroup = rsText.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAmountToBePaid(): Double {
        return pendingPaymentsMapping.sumOf { it.amountToBePaid }.roundToTwoDecimal()
    }

}