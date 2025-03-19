package com.example.grouppay.domain.repository

import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.SquareOffTransactionModel
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun upsertExpense(expense: Expense): Boolean

    suspend fun getExpensesByGroupId(groupId: String): List<Expense>

    fun getSquareOffTransactions(objectId: String): Flow<List<SquareOffTransactionModel>>


}