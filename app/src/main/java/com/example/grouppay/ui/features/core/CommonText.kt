package com.example.grouppay.ui.features.core

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.grouppay.R

@Preview(showSystemUi = true)
@Composable
fun CommonText(
    modifier: Modifier = Modifier,
    text: String = "Common text",
    fontSize: TextUnit = 14.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.W500
) {
    Text(
        text = text,
        modifier = modifier,
        color = colorResource(id = R.color.text_color),
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle
    )
}