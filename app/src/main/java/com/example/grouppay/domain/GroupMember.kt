package com.example.grouppay.domain

data class GroupMember(
    var id: String = "",
    var name: String,
    var amountBorrowedFromGroup: Double = 0.0,
    var amountOwedFromGroup: Double = 0.0,
    var isSelected: Boolean = true,
    val paymentToBeMadeMapping: ArrayList<Pair<String, Double>> = arrayListOf()
) {
    fun setAmountBorrowedFromGroup(rsText: String) {
        try {
            amountBorrowedFromGroup = rsText.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}