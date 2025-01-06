package com.example.grouppay.domain

data class PendingPayments(
    val originalPayerId: String,
    val originalPayerName: String,
    val amountToBePaid: Double
)