package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.grouppay.R

@Composable
fun <T> AutocompleteTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    updateText: (String) -> Unit,
    suggestions: List<T>,
    selectSuggestion: (T) -> Unit,
    saveNewSuggestion: (String) -> Unit
) {

    var filteredSuggestions by remember { mutableStateOf(suggestions) }
    var showSuggestions by remember { mutableStateOf(false) }
    var showAddNew by remember { mutableStateOf(text.isNotEmpty()) }

    Column {
        CommonOutlinedTextField(
            text = text,
            updateText = { input ->
                filteredSuggestions = suggestions.filter {
                    text.contains(input, ignoreCase = true)
                }
                showSuggestions = filteredSuggestions.isNotEmpty()
                updateText(input)
            },
            hint = hint,
            modifier = Modifier.fillMaxWidth()
        )

        if (showSuggestions) {
            Column(modifier = Modifier.fillMaxWidth()) {
                filteredSuggestions.forEach { suggestion ->
                    SuggestionItem(
                        suggestion = suggestion,
                        text = text,
                        showAddNew = false
                    ) {
                        showSuggestions = false
                        updateText(text)
                        selectSuggestion(suggestion)
                    }
                }
            }
        }
        if (showAddNew) {
            SuggestionItem(
                suggestion = null,
                text = text,
                showAddNew = true
            ) {
                showAddNew = false
                saveNewSuggestion(text)
            }
        }
    }
}

@Composable
fun <T> SuggestionItem(
    suggestion: T,
    text: String,
    showAddNew: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(id = R.drawable.add), contentDescription = "")
        CommonText(
            text = text,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onClick() },
        )
    }

}