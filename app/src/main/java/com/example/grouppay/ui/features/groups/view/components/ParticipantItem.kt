package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.utils.roundToTwoDecimal


@Composable
fun ParticipantItem(
    modifier: Modifier = Modifier,
    participant: GroupMember,
    onGroupMemberClicked: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onGroupMemberClicked(participant.id)
            }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonText(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.33F),
                text = participant.name,
                fontSize = 20.sp,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Paid",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 16.sp,
                        text = "₹ ${participant.amountOwedFromGroup.roundToTwoDecimal()}",
                        textColor = if (participant.amountOwedFromGroup > 0.0) {
                            colorResource(R.color.amount_green)
                        } else if (participant.amountOwedFromGroup < 0.0) {
                            colorResource(R.color.amount_red)
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Borrowed",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 16.sp,
                        text = "₹ ${participant.amountBorrowedFromGroup.roundToTwoDecimal()}",
                        textColor = if (participant.amountBorrowedFromGroup > 0.0) {
                            colorResource(R.color.amount_red)
                        } else if (participant.amountBorrowedFromGroup < 0.0) {
                            colorResource(R.color.amount_green)
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
            }
        }
    }
}
