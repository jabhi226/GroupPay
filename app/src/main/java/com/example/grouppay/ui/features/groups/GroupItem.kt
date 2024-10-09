package com.example.grouppay.ui.features.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import org.mongodb.kbson.BsonObjectId

@Preview
@Composable
fun GroupItem(
    modifier: Modifier = Modifier,
    group: GroupWithTotalExpense =
        GroupWithTotalExpense(
            BsonObjectId(),
            "Karjat trip",
            3,
            100.0
        ),
    navigateToGroup: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToGroup() }
            .background(
                colorResource(id = R.color.background_light_color),
                shape = RoundedCornerShape(16.dp)
            ))
    {
        Column(modifier = Modifier.padding(16.dp)) {
            CommonText(
                modifier = Modifier,
                text = group.groupName,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(
                modifier = Modifier,
                text = "Total Amount Spend ₹${group.totalAmountSpent}",
                fontSize = 22.sp,
                fontWeight = FontWeight.W500,
                textColor = colorResource(R.color.accent_color)
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommonText(
                modifier = Modifier,
                text = "Total Members: ${group.totalMembers}",
                fontSize = 22.sp,
                fontWeight = FontWeight.W500,
                fontStyle = FontStyle.Italic,
                textColor = colorResource(R.color.light_text_color)
            )
        }
    }
}