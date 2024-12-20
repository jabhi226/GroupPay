package com.example.grouppay.ui.features.core.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.grouppay.R

@Preview(showSystemUi = true)
@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String = "Add User",
    onClick: () -> Unit = {}
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.accent_color)),
        onClick = { }) {
        CommonText(text = text, textColor = colorResource(id = R.color.text_color))
    }
}