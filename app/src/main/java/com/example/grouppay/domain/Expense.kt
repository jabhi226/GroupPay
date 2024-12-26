package com.example.grouppay.domain


data class Expense(
    var id: String,
    var label: String,
    var paidBy: Participant?,
    var dateOfExpense: Long?,
    var remainingParticipants: List<Participant>,
    var group: Group? = null
)