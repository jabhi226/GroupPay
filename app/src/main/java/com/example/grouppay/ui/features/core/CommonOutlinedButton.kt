package com.example.grouppay.ui.features.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    updateText: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            updateText(it)
        },
        label = { CommonText(text = "") },
        readOnly = true,
        shape = RoundedCornerShape(12.dp)
    )
}