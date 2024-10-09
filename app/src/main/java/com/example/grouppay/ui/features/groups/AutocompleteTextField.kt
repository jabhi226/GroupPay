package com.example.grouppay.ui.features.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.core.CommonText

@Composable
fun AutocompleteTextField(modifier: Modifier = Modifier, suggestions: List<Participant>) {

    var text by remember { mutableStateOf("") }
    var filteredSuggestions by remember { mutableStateOf(suggestions) }
    var showSuggestions by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { input ->
                text = input
                filteredSuggestions = suggestions.filter {
                    it.name.contains(input, ignoreCase = true)
                }
                showSuggestions = filteredSuggestions.isNotEmpty()
            },
            label = { CommonText(text = "app") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showSuggestions) {
            Column(modifier = Modifier.fillMaxWidth()) {
                filteredSuggestions.forEach { suggestion ->
                    SuggestionItem(suggestion) {
                        text = suggestion.name
                        showSuggestions = false
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(suggestion: Participant, onClick: () -> Unit) {
    CommonText(
        text = suggestion.name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
    )
}