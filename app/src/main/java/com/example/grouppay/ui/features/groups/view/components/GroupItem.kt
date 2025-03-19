package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense

@Composable
fun GroupItem(
    modifier: Modifier = Modifier,
    group: GroupWithTotalExpense,
    navigateToGroup: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToGroup() }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ))
    {
        Column(modifier = Modifier.padding(16.dp)) {
            CommonText(
                modifier = Modifier,
                text = group.groupName,
                fontSize = 22.sp,
                fontWeight = FontWeight.W600,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(
                modifier = Modifier,
                text = "Total Amount Spend: ₹${group.totalAmountSpent}",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                textColor = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommonText(
                modifier = Modifier,
                text = "Total Members: ${group.totalMembers}",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                fontStyle = FontStyle.Italic,
                textColor = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}