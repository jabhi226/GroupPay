package com.example.grouppay.ui.features.addExpense.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.ui.features.core.view.components.AutocompleteTextField
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.EmptyScreen
import com.example.grouppay.ui.features.utils.formatToTwoDecimalPlaces
import com.example.grouppay.ui.features.utils.showToast
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    groupId: String?
) {
    val viewModel: AddExpenseViewModel = koinViewModel()
    LaunchedEffect(groupId) {
        viewModel.getParticipantsByGroupId(groupId)
    }
    val allParticipantsByGroupId by viewModel.allParticipantsByGroupId.collectAsState()
    val paidBy by viewModel.paidBy.collectAsState()
    val totalAmountPaid by viewModel.totalAmountPaid.collectAsState()
    val expenseName by viewModel.expenseName.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.uiEvents) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                AddExpenseViewModel.UiEvents.NavigateUp -> {
                    context.showToast("Expense $expenseName added.")
                    navController.navigateUp()
                }

                is AddExpenseViewModel.UiEvents.ShowError -> {
                    context.showToast(event.error)
                }
            }
        }
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    CommonText(
                        text = "Add Expense",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.saveExpense()
                }) {
                    CommonText(text = "Save")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                CommonOutlinedTextField(
                    text = expenseName,
                    hint = "Expense Name, ex. Lunch",
                    updateText = {
                        viewModel.updateExpenseName(it)
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))
                AutocompleteTextField(
                    text = paidBy?.name ?: "",
                    hint = "Paid by",
                    suggestions = allParticipantsByGroupId,
                    getSuggestionName = {
                        it.name
                    },
                    selectSuggestion = {
                        viewModel.updatePaidBy(it)
                    },
                    saveNewSuggestion = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonOutlinedTextField(
                    text = totalAmountPaid,
                    hint = "Total Amount Paid",
                    updateText = {
                        viewModel.updateTotalAmountPaid(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))
                CommonText(text = "Included Participants", modifier = Modifier)

                Spacer(modifier = Modifier.height(16.dp))
                if (allParticipantsByGroupId.isNotEmpty()) {
                    LazyColumn {
                        itemsIndexed(allParticipantsByGroupId) { index, participant ->
                            ParticipantContributions(
                                participant = participant,
                                index = index,
                                totalAmountPaid = totalAmountPaid.toDoubleOrNull() ?: 0.0,
                                totalParticipants = allParticipantsByGroupId.count { it.isSelected },
                                updateParticipantAmount = { viewModel.updateParticipantAmount(it) },
                                updateParticipantSelection = {
                                    viewModel.updateParticipantSelection(
                                        it.id
                                    )
                                },
                            )
                        }
                    }
                } else {
                    EmptyScreen(text = "No group members found to make expense.")
                }
            }
        }
    }
}

@Composable
fun ParticipantContributions(
    modifier: Modifier = Modifier,
    participant: ExpenseMember,
    index: Int,
    totalAmountPaid: Double,
    totalParticipants: Int,
    updateParticipantAmount: (ExpenseMember) -> Unit = {},
    updateParticipantSelection: (ExpenseMember) -> Unit = {}
) {
    var rsText by remember { mutableStateOf("") }
    var perText by remember { mutableStateOf("") }
    var isSelected by remember { mutableStateOf(false) }

    LaunchedEffect(totalAmountPaid, participant.isSelected) {
        isSelected = participant.isSelected
        rsText =
            if (isSelected) (totalAmountPaid / totalParticipants).formatToTwoDecimalPlaces() else "0.0"
        perText =
            if (isSelected) ((totalAmountPaid / totalParticipants) / totalAmountPaid * 100).formatToTwoDecimalPlaces() else "0.0"
    }

    LaunchedEffect(perText) {
        try {
            rsText = ((totalAmountPaid * perText.toDouble()) / 100).formatToTwoDecimalPlaces()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    LaunchedEffect(rsText) {
        try {
            perText = ((rsText.toDouble() / totalAmountPaid) * 100).formatToTwoDecimalPlaces()
            updateParticipantAmount(participant.apply {
                this.setAmountBorrowedForExpense(rsText)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(participant.amountBorrowedForExpense) {
        println("====> ${participant.name} | ${participant.amountBorrowedForExpense}")
        rsText = participant.amountBorrowedForExpense.toString()
    }

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CommonText(
                text = "${index + 1}: ${participant.name}",
                textColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = {
                    updateParticipantSelection(participant)
                },
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                CommonOutlinedTextField(
                    modifier = Modifier,
                    text = rsText,
                    hint = "Amount in ₹",
                    updateText = {
                        rsText = (if (it.endsWith(".")) it.replace(".", "") else it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        updateParticipantAmount(participant.apply {
                            this.setAmountBorrowedForExpense(rsText)
                        })
                    })
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                CommonOutlinedTextField(
                    modifier = Modifier,
                    text = perText,
                    hint = "Amount in %",
                    updateText = {
                        perText = (if (it.endsWith(".")) it.replace(".", "") else it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        updateParticipantAmount(participant.apply {
                            this.setAmountBorrowedForExpense(rsText)
                        })
                    })
                )
            }
        }
    }
}