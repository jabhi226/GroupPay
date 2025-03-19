package com.example.grouppay.ui.features.core.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grouppay.ui.features.core.view.components.CommonText

@Composable
fun EmptyScreen(modifier: Modifier = Modifier, text: String) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CommonText(
            fontSize = 18.sp,
            textColor = MaterialTheme.colorScheme.onBackground,
            text = text
        )
    }
}