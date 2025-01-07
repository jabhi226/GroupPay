package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.EmptyScreen
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.features.groups.viewmodel.SquareOffUtils
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettlementScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: GroupViewModel = koinViewModel()
    val squareOffTransaction by viewModel.squareOffTransactions.collectAsState()
    LaunchedEffect(group) {
        viewModel.getSquareOffTransactions(group)
    }
    var isShowSquareOff by remember { mutableStateOf(false) }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    isShowSquareOff = !isShowSquareOff
                }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = if (!isShowSquareOff) R.drawable.ic_settlement else R.drawable.ic_settlement),
                            contentDescription = "add_settlement"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = if (!isShowSquareOff) "Settle up" else "Save",
                        )
                    }
                }
            }) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding),
            ) {
                if (squareOffTransaction.isEmpty()) {
                    EmptyScreen(text = "All transactions are squared off.")
                } else {
                    LazyColumn {
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
}


@Preview(showBackground = true)
@Composable
fun SettlementItem(
    modifier: Modifier = Modifier,
    participant: SquareOffTransactionModel = SquareOffUtils.getSquareOffTransaction()[0],
    isShowSquareOff: Boolean = true,
    squareOffTransaction: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseSurface,
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
            text = "${participant.senderMember.name} will pay ₹ ${participant.amount} to ${participant.receiverMember.name}",
            fontSize = 18.sp,
            textColor = MaterialTheme.colorScheme.inverseOnSurface
        )
        if (isShowSquareOff) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        squareOffTransaction()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.ic_settlement),
                    contentDescription = "squareoff",
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }

}