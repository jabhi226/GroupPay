package com.example.grouppay.ui.features.groupDetails

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.R
import com.example.grouppay.domain.Splitter
import com.example.grouppay.ui.features.core.CommonText

@Preview
@Composable
fun Ower(modifier: Modifier = Modifier, user: Splitter = getContros()[0].owers[0]) {
    Box(
        modifier = modifier.background(
            color = colorResource(id = R.color.background_light_color),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            CommonText(
                text = "Name: ${user.userName}",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
            CommonText(
                text = "Amount Owns: ₹ ${user.amountBorrowed}",
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
            )
        }
    }
}