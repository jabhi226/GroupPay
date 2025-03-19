package com.example.grouppay.ui.features.groups.view.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.example.grouppay.R
import com.example.grouppay.ui.features.core.view.components.CommonAlertDialog
import com.example.grouppay.ui.features.core.view.components.CommonButton
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.utils.roundToTwoDecimal


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
                color = MaterialTheme.colorScheme.secondaryContainer,
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
            textColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        if (isShowSquareOff) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
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
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        if (showConfirmationDialog) {
            CommonAlertDialog(
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