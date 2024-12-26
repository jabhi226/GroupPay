package com.example.grouppay.domain


data class Group(
    var id: String,
    var name: String,
    var expenses: List<Expense>,
    var participants: List<GroupMember>
)