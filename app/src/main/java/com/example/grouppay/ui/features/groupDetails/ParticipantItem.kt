package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.core.CommonText

@Preview
@Composable
fun ParticipantItem(
    modifier: Modifier = Modifier,
    participant: Participant = getContros()[0].remainingParticipants[0]
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = colorResource(id = R.color.background_color),
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
                modifier = Modifier.padding(16.dp),
                text = participant.name,
                fontSize = 22.sp
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CommonText(
                    text = "Paid",
                    textColor = colorResource(R.color.accent_color)
                )
                Spacer(modifier = Modifier.height(4.dp))
                CommonText(
                    text = "₹${participant.amountOwedFromGroup}",
                    textColor = colorResource(R.color.accent_color)
                )
            }
        }
    }
}