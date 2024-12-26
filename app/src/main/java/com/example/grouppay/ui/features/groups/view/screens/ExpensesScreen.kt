package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun ExpensesScreen(navController: NavController, group: Group) {
    val viewModel: GroupViewModel = koinViewModel()
    val expenses by viewModel.expenses.collectAsState()
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_expense),
                        contentDescription = "add_user"
                    )
                    CommonText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = "Add Expense"
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
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

@Preview
@Composable
fun ExpenseComponent(modifier: Modifier = Modifier, expense: Expense = Testing.getExpense()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CommonText(
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            text = expense.label,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        expense.paidBy?.let { participant ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                CommonText(
                    textColor = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp,
                    text = "Paid by: ${participant.name}"
                )
            }
        }

        // Date of Expense
        CommonText(
            textColor = MaterialTheme.colorScheme.secondary,
            fontStyle = FontStyle.Italic,
            text = "Date: ${SimpleDateFormat("yyyy MMM dd, hh:mm").format(Date(expense.dateOfExpense))}",
            modifier = Modifier.padding(top = 4.dp)
        )

        // Remaining Participants
        if (expense.remainingParticipants.isNotEmpty()) {
            CommonText(
                text = "Remaining Participants:",
                modifier = Modifier.padding(top = 16.dp),
                textColor = MaterialTheme.colorScheme.tertiary,
                fontSize = 16.sp,
            )
            LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                items(expense.remainingParticipants) { participant ->
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.tertiaryContainer),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        CommonText(
                            text = participant.name,
                            fontSize = 16.sp,
                            textColor = MaterialTheme.colorScheme.tertiary
                        )
                        CommonText(
                            text = "Borrowed: ₹ ${participant.amountBorrowedForExpense}",
                            fontSize = 14.sp,
                            textColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

//        // Group
//        expense.group?.let {
//            CommonText(
//                text = "Group: ${it.name}",
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
    }
}