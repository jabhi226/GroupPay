package com.example.grouppay.ui.features.groups.model

import com.example.grouppay.domain.GroupMember

data class SquareOffTransactionModel(
    val senderMember: GroupMember,
    val receiverMember: GroupMember,
    val amount: Double
)
