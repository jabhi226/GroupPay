package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grouppay.R

@Composable
fun <T> AutocompleteTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    suggestions: List<T>,
    getSuggestionName: (T) -> String,
    selectSuggestion: (T) -> Unit,
    saveNewSuggestion: (String) -> Unit,
) {

    var updatedText by remember { mutableStateOf("") }
    var filteredSuggestions by remember { mutableStateOf(listOf<T>()) }
    var showSuggestions by remember { mutableStateOf(false) }
    var showAddNew by remember { mutableStateOf(false) }

    LaunchedEffect(text) {
        updatedText = text
    }

    LaunchedEffect(updatedText) {
        if (updatedText.isEmpty() || updatedText == text) {
            filteredSuggestions = listOf()
            showSuggestions = false
            showAddNew = false
        } else {
            filteredSuggestions = suggestions.filter {
                getSuggestionName(it).contains(updatedText, ignoreCase = true)
            }
            showSuggestions = true
            showAddNew = true
        }
    }

    Column {
        CommonOutlinedTextField(
            text = updatedText,
            updateText = { input ->
                updatedText = input
            },
            hint = hint,
            modifier = Modifier.fillMaxWidth()
        )

        if (showSuggestions) {
            LazyColumn {
                items(filteredSuggestions) { suggestion ->
                    SuggestionItem(
                        text = getSuggestionName(suggestion),
                        showAddNew = false
                    ) {
                        selectSuggestion(suggestion)
                    }
                }
            }
        }
        if (showAddNew) {
            SuggestionItem(
                text = updatedText,
                showAddNew = true
            ) {
                saveNewSuggestion(updatedText)
            }
        }
    }
}


@Preview
@Composable
fun SuggestionItem(
    text: String = "Test",
    showAddNew: Boolean = true,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (showAddNew) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = ""
            )
        }
        CommonText(
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            text = text,
            modifier = Modifier
                .padding(horizontal = 16.dp),
        )
    }

}