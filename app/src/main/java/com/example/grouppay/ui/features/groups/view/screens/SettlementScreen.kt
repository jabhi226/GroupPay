package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.core.view.components.CommonButton
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.EmptyScreen
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettlementScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: GroupViewModel = koinViewModel()
    val squareOffTransaction by viewModel.squareOffTransactions.collectAsState()
    var isShowSquareOff by remember { mutableStateOf(false) }

    LaunchedEffect(group.id) {
        viewModel.getSquareOffTransactions(group.id)
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (squareOffTransaction.isNotEmpty()) {
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
            if (squareOffTransaction.isEmpty()) {
                EmptyScreen(text = "All transactions are squared off.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    userScrollEnabled = true
                ) {
                    items(squareOffTransaction) { participant ->
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


@Composable
fun SettlementItem(
    modifier: Modifier = Modifier,
    participant: SquareOffTransactionModel,
    isShowSquareOff: Boolean = true,
    squareOffTransaction: () -> Unit = {}
) {

    var showConfirmationDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CommonText(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.8f),
            text = "${participant.senderMember.name} will pay ₹ ${participant.amount.roundToTwoDecimal()} to ${participant.receiverMember.name}.",
            fontSize = 18.sp,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        if (isShowSquareOff) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .clip(shape = RoundedCornerShape(100.dp))
                    .clickable {
                        showConfirmationDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.ic_settlement),
                    contentDescription = "squareoff",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = {
                    showConfirmationDialog = false
                },
                text = {
                    CommonText(
                        text = "Do you want to square off this transaction?"
                    )
                },
                confirmButton = {
                    CommonButton(
                        text = "Ok",
                        onClick = {
                            showConfirmationDialog = false
                            squareOffTransaction()
                        }
                    )
                },
                dismissButton = {
                    CommonButton(
                        text = "Cancel",
                        onClick = {
                            showConfirmationDialog = false
                        }
                    )
                }
            )
        }
    }

}