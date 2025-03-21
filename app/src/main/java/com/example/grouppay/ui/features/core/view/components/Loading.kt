package com.example.grouppay.ui.features.core.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(1.4F)
        )
    }
}