package com.example.grouppay.ui.features.participantDetails.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.ui.features.core.view.components.CommonText


@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    title: String = "Balance",
    value: String = "123",
    valueColorResourceId: Color
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(width = 160.dp, height = 120.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonText(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(
                text = "₹$value",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textColor = valueColorResourceId
            )
        }
    }
}