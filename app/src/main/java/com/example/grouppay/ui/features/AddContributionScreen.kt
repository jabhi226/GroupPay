package com.example.grouppay.ui.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grouppay.domain.Splitter
import com.example.grouppay.ui.features.core.AutocompleteTextField

@Composable
fun AddContributionScreen(navController: NavController) {
    val suggestions = listOf("Apple", "Banana", "Orange", "Grape", "Pineapple")
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AutocompleteTextField(suggestions = suggestions.map {
                Splitter(
                    userId = 1,
                    userName = it,
                    100.0
                )
            })
        }
    }
}