package com.example.grouppay.domain

data class GroupWithTotalExpense(
    val id: String,
    val groupName: String,
    val totalMembers: Int,
    val totalAmountSpent: Double
)
