package com.example.grouppay.ui.features.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    updateText: (String) -> Unit
) {
    val showKeyboard = remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusable(true)
            .focusRequester(focusRequester = focusRequester)
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default,
        singleLine = true,
        value = text,
        onValueChange = {
            updateText(it)
        },
        label = {
            CommonText(
                text = hint,
                textColor = colorResource(id = android.R.color.tab_indicator_text)
            )
        },
        readOnly = true,
        shape = RoundedCornerShape(12.dp)
    )

    LaunchedEffect(focusRequester) {
        if (showKeyboard.equals(true)) {
            focusRequester.requestFocus()
            delay(100)
            keyboard?.show()
        }
    }
}