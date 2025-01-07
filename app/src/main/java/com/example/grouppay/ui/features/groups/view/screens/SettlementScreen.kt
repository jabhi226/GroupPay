package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettlementScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: GroupViewModel = koinViewModel()
    LaunchedEffect(group) {
        viewModel.getSquareOffTransactions(group)
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
//                    navController.navigate("add_participant/${group.id}")
                }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settlement),
                            contentDescription = "add_settlement"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Add Participant"
                        )
                    }
                }
            }) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                group.participants.forEach { participant ->
                    val payments = participant.pendingPaymentsMapping.groupBy { it.originalPayerId }
                    for (payment in payments) {
                        payment.key //originalPayerId
                        payment.value.map { payments1 -> group.participants.find { it.id == payments1.originalPayerId } }
                    }
                    CommonText(
                        modifier = Modifier.padding(8.dp),
                        text = participant.pendingPaymentsMapping.groupBy { it.originalPayerId }
                            .map { (key, value) ->
                                "${participant.name} will pay ₹ ${value.sumOf { it.amountToBePaid }} to ${value.first().originalPayerName} ($key)\n"
                            }.joinToString() + "\n\n"
                    )
                }
            }
        }
    }
}