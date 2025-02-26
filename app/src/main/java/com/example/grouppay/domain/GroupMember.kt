package com.example.grouppay.domain

import com.example.grouppay.ui.features.utils.roundToTwoDecimal

data class GroupMember(
    var id: String = "",
    var name: String,
    var amountBorrowedFromGroup: Double = 0.0,
    var amountOwedFromGroup: Double = 0.0,
    var isSelected: Boolean = true,
    val pendingPaymentsMapping: ArrayList<PendingPayments> = arrayListOf()
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