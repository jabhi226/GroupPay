package com.example.grouppay.domain

data class Expense(
    var id: String = "",
    var label: String,
    var paidBy: ExpenseMember? = null,
    var totalAmountPaid: Double = 0.0,
    var dateOfExpense: Long = System.currentTimeMillis(),
    var remainingParticipants: List<ExpenseMember>,
    var groupId: String,
    val isSquareOff: Boolean = false
)