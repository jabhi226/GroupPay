package com.example.grouppay.domain.entities

data class PendingPayments(
    val originalPayerId: String,
    val originalPayerName: String,
    val amountToBePaid: Double
)