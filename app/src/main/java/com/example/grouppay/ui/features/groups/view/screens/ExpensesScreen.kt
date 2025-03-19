package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.screen.EmptyScreen
import com.example.grouppay.ui.features.groups.viewmodel.ExpensesViewModel
import com.example.grouppay.ui.features.utils.getDateInStringFormat
import com.example.grouppay.ui.features.utils.getInitials
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
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

@Composable
fun ExpenseComponent(modifier: Modifier = Modifier, expense: Expense) {
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

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
                Row(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (expense.label == "Square Off") {
                                    colorResource(R.color.amount_green_transparent)
                                } else {
                                    colorResource(R.color.amount_red_transparent)
                                },
                                shape = RoundedCornerShape(200.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Icon(
                            modifier = Modifier.scale(0.8F),
                            tint = if (expense.label == "Square Off") {
                                colorResource(R.color.amount_green)
                            } else {
                                colorResource(R.color.amount_red)
                            },
                            painter = painterResource(
                                id =
                                if (expense.label == "Square Off") {
                                    R.drawable.ic_squreoff
                                } else {
                                    R.drawable.ic_payments
                                }
                            ),
                            contentDescription = "",
                        )
                    }
                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CommonText(
                                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                text = expense.label,
                                fontSize = 18.sp,
                            )
                            CommonText(
                                textColor = Color(0xFF85BB65),
                                fontSize = 24.sp,
                                text = "₹ ${expense.totalAmountPaid.roundToTwoDecimal()}"
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
                                fontSize = 16.sp,
                                text = if (expense.paidBy == null) "Unpaid" else "Paid by: ${expense.paidBy?.name}"
                            )

                            CommonText(
                                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontStyle = FontStyle.Italic,
                                text = "Date: ${expense.dateOfExpense.getDateInStringFormat()}",
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                }

                if (expense.remainingParticipants.count { it.amountBorrowedForExpense > 0.0 } > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                        items(expense.remainingParticipants.filter { it.amountBorrowedForExpense > 0.0 }) { participant ->
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
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(100.dp)
                                            )
                                            .padding(8.dp)
                                            .widthIn(min = 24.dp)
                                            .heightIn(min = 24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CommonText(
                                            text = participant.name
                                                .getInitials(),
                                            fontSize = 22.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        CommonText(
                                            text = participant.name,
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