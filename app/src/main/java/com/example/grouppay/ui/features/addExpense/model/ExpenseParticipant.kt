package com.example.grouppay.ui.features.addExpense.model

import com.example.grouppay.domain.Participant

data class ExpenseParticipant(
    var isSelected: Boolean = true,
    var participant: Participant? = null
)
