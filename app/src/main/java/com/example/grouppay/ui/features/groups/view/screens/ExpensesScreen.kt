package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.screen.EmptyScreen
import com.example.grouppay.ui.features.groups.view.components.ExpenseComponent
import com.example.grouppay.ui.features.groups.viewmodel.ExpensesViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExpensesScreen(navController: NavController, group: Group) {
    val viewModel: ExpensesViewModel = koinViewModel()
    val expenses by viewModel.expenses.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(group.id) {
        viewModel.getExpensesByGroupId(groupId = group.id)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    navController.navigate("add_expense/${group.id}")
                }) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_expense),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "add_user"
                    )
                    CommonText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = "Add Expense",
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) { _ ->
        if (expenses.isEmpty()) {
            val txt = if (group.participants.isEmpty()) {
                "Please add group members before making expense."
            } else {
                "No Expense are found with ${group.name} group."
            }
            EmptyScreen(text = txt)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(expenses) {
                    ExpenseComponent(expense = it)
                }
                item { Spacer(modifier = Modifier.padding(40.dp)) }
            }
        }
    }
}