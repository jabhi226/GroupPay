package com.example.grouppay.ui.features.addExpense.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.ui.features.addExpense.view.component.ParticipantContributions
import com.example.grouppay.ui.features.addExpense.viewmodel.AddExpenseViewModel
import com.example.grouppay.ui.features.core.view.components.AutocompleteTextField
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.screen.EmptyScreen
import com.example.grouppay.ui.features.utils.showToast
import com.example.grouppay.ui.theme.GroupPayTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController, groupId: String?
) {
    val viewModel: AddExpenseViewModel = koinViewModel()
    LaunchedEffect(groupId) {
        viewModel.getParticipantsByGroupId(groupId)
    }
    val allParticipantsByGroupId by viewModel.allParticipantsByGroupId.collectAsState()
    val paidBy by viewModel.paidBy.collectAsState()
    var totalAmountPaid by remember { mutableStateOf("") }
    var expenseName by remember { mutableStateOf("") }
    val context = LocalContext.current

    val focusRequesters =
        remember { mutableStateListOf(FocusRequester(), FocusRequester(), FocusRequester()) }
    var membersRequesters by remember { mutableStateOf(listOf<FocusRequester>()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
        delay(100)
        keyboardController?.show()
    }

    LaunchedEffect(allParticipantsByGroupId.size) {
        membersRequesters = allParticipantsByGroupId.map {
            FocusRequester()
        }
    }

    LaunchedEffect(totalAmountPaid) {
        println("==> $totalAmountPaid")
    }

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
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                CommonText(
                    text = "Add Expense", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            })
        }, floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 40.dp),
                onClick = {
                    viewModel.saveExpense(expenseName)
                }) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_expense),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "add_expense"
                    )
                    CommonText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = "Save Expense",
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {
                item {
                    CommonOutlinedTextField(
                        modifier = Modifier.focusRequester(focusRequesters[0]),
                        text = expenseName,
                        hint = "Expense Name, ex. Lunch",
                        updateText = {
                            expenseName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                            }
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    AutocompleteTextField(
                        modifier = Modifier.focusRequester(focusRequesters[1]).fillMaxHeight(0.3F),
                        text = paidBy.name,
                        hint = "Paid by",
                        suggestions = allParticipantsByGroupId,
                        getSuggestionName = { it.name },
                        selectSuggestion = {
                            viewModel.updatePaidBy(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequesters[2].requestFocus()
                            },
                        ),
                        saveNewSuggestion = {
                            viewModel.saveNewMember(groupId, it)
                        },
                        newSuggestionConfirmationMessage = "Do you want add a new member?"
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    CommonOutlinedTextField(
                        modifier = Modifier.focusRequester(focusRequesters[2]),
                        text = totalAmountPaid,
                        hint = "Total Amount Paid",
                        updateText = {
                            totalAmountPaid = it
                        },
                        maxCharacterLength = 6,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                membersRequesters.getOrNull(0)?.requestFocus()
                            },
                        ),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    CommonText(text = "Included Members", modifier = Modifier)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (allParticipantsByGroupId.isNotEmpty()) {
                    itemsIndexed(allParticipantsByGroupId) { index, participant ->
                        ParticipantContributions(
                            modifier = Modifier.focusRequester(
                                membersRequesters.getOrNull(index) ?: FocusRequester.Default
                            ),
                            participant = participant,
                            index = index,
                            totalAmountPaid = totalAmountPaid,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (index < membersRequesters.size - 1) {
                                        membersRequesters.getOrNull(index + 1)?.requestFocus()
                                    }
                                },
                                onDone = {
                                    membersRequesters.getOrNull(index)?.freeFocus()
                                }
                            ),
                            totalSelectedParticipants = allParticipantsByGroupId.filter { it.isSelected },
                            updateParticipantAmount = {
                                viewModel.updateParticipantAmount(it, totalAmountPaid)
                            },
                            updateParticipantSelection = {
                                viewModel.updateParticipantSelection(
                                    it.groupMemberId,
                                    totalAmountPaid
                                )
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                } else {
                    item {
                        EmptyScreen(text = "No group members found to make expense.")
                    }
                }
            }

        }
    }
}