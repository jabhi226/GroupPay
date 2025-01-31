package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContributionBottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    if (isBottomSheetVisible) {

        val mText = remember {
            mutableStateOf("")
        }

        ModalBottomSheet(
            modifier = Modifier.background(color = Color.White),
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RectangleShape,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = .5f),
        ) {
            AutocompleteTextField(
                text = "text",
                suggestions = listOf<String>(),
                getSuggestionName = {
                    "test"
                },
                selectSuggestion = {},
                saveNewSuggestion = {})
            CommonOutlinedTextField(text = mText.value) {
                mText.value = it
            }


        }

    }
}