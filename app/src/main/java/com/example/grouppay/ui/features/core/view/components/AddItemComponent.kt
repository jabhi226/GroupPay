package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemComponent(topBarText: String, hint: String, btnClick: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                btnClick(text)
            }) {
                CommonText(text = "Save")
            }
        },
        topBar = {
            TopAppBar(title = {
                CommonText(
                    text = topBarText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            })
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CommonOutlinedTextField(
                text = text,
                hint = hint
            ) {
                text = it
            }
        }
    }
}