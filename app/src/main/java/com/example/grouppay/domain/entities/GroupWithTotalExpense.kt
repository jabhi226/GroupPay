package com.example.grouppay.domain.entities

data class GroupWithTotalExpense(
    val id: String,
    val groupName: String,
    val totalMembers: Int,
    val totalAmountSpent: Double
)
