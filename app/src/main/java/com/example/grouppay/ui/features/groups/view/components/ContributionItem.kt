package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.core.view.components.CommonText

@Preview
@Composable
fun ContributionItem(modifier: Modifier = Modifier, contribution: Expense = Testing.getContros()[0]) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = colorResource(id = R.color.background_color),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            CommonText(
                text = contribution.label,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            CommonText(
                text = "${contribution.paidBy?.name} paid ₹${contribution.paidBy?.amountBorrowedForExpense}",
                fontSize = 16.sp,
                textColor = colorResource(R.color.light_text_color)
            )
//            Spacer(modifier = Modifier.height(4.dp))
//            CommonText(text = "Owers:", fontSize = 16.sp, fontStyle = FontStyle.Italic)
//            Spacer(modifier = Modifier.height(4.dp))
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = modifier
//                    .fillMaxSize()
//            ) {
//                items(contribution.remainingParticipants) {
//                    Ower(modifier = modifier, user = it)
//                }
//            }
        }
    }
}