package com.example.grouppay.domain


data class Expense(
    var id: String = "",
    var label: String,
    var paidBy: Participant?,
    var dateOfExpense: Long = System.currentTimeMillis(),
    var remainingParticipants: List<Participant>,
    var groupId: String
)