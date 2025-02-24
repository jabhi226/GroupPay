package com.example.grouppay.ui.features.core.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CommonAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)?,
    text: @Composable (() -> Unit)?
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        text = text,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    )
}