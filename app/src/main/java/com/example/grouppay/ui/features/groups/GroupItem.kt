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
import com.example.grouppay.domain.GroupInfo

@Composable
fun GroupItem(
    modifier: Modifier = Modifier,
    group: GroupInfo,
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
                fontWeight = FontWeight.W700
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(
                modifier = Modifier,
                text = "Total Amount Spend",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(4.dp))
            CommonText(
                modifier = Modifier,
                text = "₹ ${group.contributions.sumOf { it.paidBy?.amountOwed ?: 0.0 }}",
                fontSize = 28.sp,
                fontWeight = FontWeight.W700
            )
        }
    }
}