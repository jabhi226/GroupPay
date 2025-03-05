package com.example.grouppay.domain.entities

data class ExpenseMember(
    val id: String = "",
    val groupExpenseId: String = "",
    val groupMemberId: String,
    val name: String,
    var amountBorrowedForExpense: Double = 0.0,
    var amountOwedForExpense: Double = 0.0,
    var isSelected: Boolean = true
) {
    fun setAmountBorrowedForExpense(rsText: String) {
        try {
            amountBorrowedForExpense = rsText.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}