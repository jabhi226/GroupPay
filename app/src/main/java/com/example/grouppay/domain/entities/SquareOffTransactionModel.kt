package com.example.grouppay.domain.entities

data class SquareOffTransactionModel(
    val senderMember: GroupMember,
    val receiverMember: GroupMember,
    val amount: Double
)
