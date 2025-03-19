package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.domain.entities.SquareOffTransactionModel
import com.example.grouppay.ui.features.core.view.screen.EmptyScreen
import com.example.grouppay.ui.features.core.view.components.Loading
import com.example.grouppay.ui.features.groups.view.components.SettlementItem
import com.example.grouppay.ui.features.groups.viewmodel.SquareOffViewModel
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettlementScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: SquareOffViewModel = koinViewModel()
    val squareOffTransaction by viewModel.squareOffTransactions.collectAsState()
    var isShowSquareOff by remember { mutableStateOf(false) }

    LaunchedEffect(group.id) {
        viewModel.getSquareOffTransactions(group.id)
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (squareOffTransaction is SettlementScreenUiState.SettlementList) {
                    FloatingActionButton(
                        onClick = {
                            isShowSquareOff = !isShowSquareOff
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = if (!isShowSquareOff) R.drawable.ic_settlement else R.drawable.ic_settlement),
                                contentDescription = "add_settlement",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            CommonText(
                                modifier = Modifier.padding(start = 12.dp),
                                text = if (!isShowSquareOff) "Square off" else "Save Square off",
                                textColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }) { _ ->

            when (squareOffTransaction) {

                is SettlementScreenUiState.Error -> {
                    EmptyScreen(
                        text = (squareOffTransaction as? SettlementScreenUiState.Error)?.error
                            ?: "Something went wrong"
                    )
                }

                SettlementScreenUiState.Loading -> {
                    Loading()
                }

                is SettlementScreenUiState.SettlementList -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                        userScrollEnabled = true
                    ) {
                        items(
                            (squareOffTransaction as? SettlementScreenUiState.SettlementList)?.list
                                ?: listOf()
                        ) { participant ->
                            SettlementItem(
                                participant = participant,
                                isShowSquareOff = isShowSquareOff
                            ) {
                                viewModel.squareOffTransaction(participant, group.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class SettlementScreenUiState {
    data object Loading : SettlementScreenUiState()
    data class Error(val error: String) : SettlementScreenUiState()
    data class SettlementList(val list: List<SquareOffTransactionModel>) : SettlementScreenUiState()
}