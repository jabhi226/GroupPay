package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.grouppay.ui.features.core.view.components.EmptyScreen
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.features.utils.getDateInStringFormat
import com.example.grouppay.ui.features.utils.getInitials
import org.koin.androidx.compose.koinViewModel


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
                        painter = painterResource(id = R.drawable.ic_add_expense),
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
        if (expenses.isEmpty()) {
            EmptyScreen(
                text = "No Expense are found with ${group.name} group."
            )
        } else {
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
}

@Preview
@Composable
fun ExpenseComponent(modifier: Modifier = Modifier, expense: Expense = Testing.getExpense()) {
    Box(modifier = Modifier.background(Color.White)) {

        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CommonText(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            text = expense.label,
                            fontSize = 20.sp,
                        )
                        CommonText(
                            textColor = Color(0xFF85BB65),
                            fontSize = 26.sp,
                            text = "₹ ${expense.totalAmountPaid}"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CommonText(
                            textColor = MaterialTheme.colorScheme.tertiary,
                            fontSize = 18.sp,
                            text = if (expense.paidBy == null) "Unpaid" else "Paid by: ${expense.paidBy?.name?.ifEmpty { "Abhishek" }}"
                        )

                        CommonText(
                            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontStyle = FontStyle.Italic,
                            text = "Date: ${expense.dateOfExpense.getDateInStringFormat()}",
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                if (expense.remainingParticipants.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                        items(expense.remainingParticipants) { participant ->
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(100.dp)
                                            )
                                            .padding(8.dp)
                                            .widthIn(min = 24.dp)
                                            .heightIn(min = 24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CommonText(
                                            text = participant.name.ifEmpty { "Abhishek Ja" }
                                                .getInitials(),
                                            fontSize = 22.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        CommonText(
                                            text = participant.name.ifEmpty { "Siddhesh" },
                                            fontSize = 16.sp,
                                            textColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        CommonText(
                                            text = "₹ ${participant.amountBorrowedForExpense}",
                                            fontSize = 14.sp,
                                            textColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}