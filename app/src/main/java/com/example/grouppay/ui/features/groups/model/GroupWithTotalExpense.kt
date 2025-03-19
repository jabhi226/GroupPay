package com.example.grouppay.ui.features.groups.model

data class GroupWithTotalExpense(
    val id: String,
    val groupName: String,
    val totalMembers: Int,
    val totalAmountSpent: Double
)
