package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    singleLine: Boolean = true,
    maxCharacterLength: Int = 100,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    updateText: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusable(true),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        value = text,
        onValueChange = {
            if (it.length <= maxCharacterLength)
                updateText(it)
        },
        label = {
            CommonText(
                text = hint, textColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
        shape = RoundedCornerShape(12.dp),
    )
}