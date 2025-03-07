package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.ui.features.core.view.components.CommonText

@Composable
fun Ower(
    modifier: Modifier = Modifier,
    user: GroupMember
) {
    Box(
        modifier = modifier.background(
            color = colorResource(id = R.color.background_light_color),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            CommonText(
                text = "Name: ${user.name}",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
            CommonText(
                text = "Amount Owns: ₹ ${user.amountBorrowedFromGroup}",
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
            )
        }
    }
}