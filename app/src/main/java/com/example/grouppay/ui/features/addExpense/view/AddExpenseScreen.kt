package com.example.grouppay.ui.features.addExpense.view

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.core.view.components.AutocompleteTextField
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(groupId: String?) {
    val viewModel: AddExpenseViewModel = koinViewModel()
    LaunchedEffect(groupId) {
        viewModel.getParticipantsByGroupId(groupId)
    }
    val allParticipantsByGroupId by viewModel.allParticipantsByGroupId.collectAsState()
    val paidBy by viewModel.paidBy.collectAsState()

    var expenseName by remember { mutableStateOf("") }
    var totalAmountPaid by remember { mutableDoubleStateOf(0.0) }
    var paidByText by remember { mutableStateOf(paidBy?.name ?: "") }


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
                    .padding(8.dp)
            ) {

                CommonOutlinedTextField(
                    text = expenseName,
                    hint = "Expense Name, ex. Lunch",
                    updateText = {
                        expenseName = it
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))
                AutocompleteTextField(
                    text = paidByText,
                    hint = "Paid by",
                    updateText = {
                        paidByText = it
                    },
                    suggestions = allParticipantsByGroupId,
                    selectSuggestion = {
                        viewModel.updatePaidBy(it)
                    },
                    saveNewSuggestion = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
//                CommonOutlinedTextField(
//                    text = expenseName,
//                    hint = "Expense Name, ex. Lunch",
//                    updateText = {
//                        expenseName = it
//                    },
//                )
                CommonOutlinedTextField(
                    text = totalAmountPaid.toString(),
                    hint = "Total Amount Paid",
                    updateText = {
                        if (it.isNotEmpty() || it.matches(Regex("^\\d+\$"))) {
                            totalAmountPaid = it.toDouble()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))
                CommonText(text = "Included Participants", modifier = Modifier)

                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    allParticipantsByGroupId.forEachIndexed { index, participant ->
                        ParticipantContributions(
                            participant = participant,
                            index = index,
                            totalAmountPaid = totalAmountPaid.toDouble(),
                            totalParticipants = allParticipantsByGroupId.size
                        ) {
                            viewModel.updateParticipant(it)
                        }
                    }
                }
            }
        }
    }
}

fun isValidNumber(input: String): Boolean {
    // Check for valid integers or floats (including edge case for `12.`)
    val regex =
        "^[+-]?\\d*\\.?\\d+$".toRegex()  // Match integers and floats with optional decimal point
    return input.matches(regex) || input.toDoubleOrNull() != null
}

@Preview(showSystemUi = true)
@Composable
fun ParticipantContributions(
    modifier: Modifier = Modifier,
    participant: Participant = Testing.getParticipent(),
    index: Int = 0,
    totalAmountPaid: Double = 50.0,
    totalParticipants: Int = 2,
    updateParticipant: (Participant) -> Unit = {}
) {
    var rsText by remember { mutableDoubleStateOf(totalAmountPaid / totalParticipants) }
    var perText by remember { mutableDoubleStateOf((totalAmountPaid / totalParticipants) / totalAmountPaid * 100) }

    fun calculateAmountFromPercentage(percentage: Double) {
        rsText = (totalAmountPaid * percentage) / 100
    }

    fun calculatePercentageFromAmount(amount: Double) {
        perText = (amount / totalAmountPaid) * 100
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
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
            Checkbox(checked = true, onCheckedChange = { it
                updateParticipant(participant.apply {
                    amountBorrowedFromGroup = rsText

                })
            })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                CommonOutlinedTextField(
                    modifier = Modifier,
                    text = rsText.toString(),
                    hint = "Amount in ₹",
                    updateText = {
                        rsText = (if (it.endsWith(".")) it.replace(".", "") else it).toDouble()
                        calculatePercentageFromAmount(rsText)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        updateParticipant(participant.apply {
                            amountBorrowedFromGroup = rsText
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
                    text = perText.toString(),
                    hint = "Amount in %",
                    updateText = {
                        perText = it.toDoubleOrNull() ?: 0.0
                        calculateAmountFromPercentage(perText)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        updateParticipant(participant.apply {
                            amountBorrowedFromGroup = rsText
                        })
                    })
                )
            }
        }
    }
}