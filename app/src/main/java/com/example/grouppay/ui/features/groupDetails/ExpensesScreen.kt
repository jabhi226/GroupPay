package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
import org.bson.types.ObjectId


@Composable
fun ExpensesScreen(group: Group) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cart Screen", style = MaterialTheme.typography.bodyLarge)
    }
}