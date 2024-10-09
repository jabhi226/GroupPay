package com.example.grouppay.ui.features.groups.model

import org.mongodb.kbson.ObjectId

data class GroupWithTotalExpense(
    val _id: ObjectId,
    val groupName: String,
    val totalMembers: Int,
    val totalAmountSpent: Double
)
