package com.example.grouppay.ui.features.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    updateText: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            updateText(it)
        },
        label = { CommonText(text = hint, textColor = colorResource(id = android.R.color.tab_indicator_text)) },
        readOnly = true,
        shape = RoundedCornerShape(12.dp)
    )
}