package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grouppay.domain.Splitter

@Composable
fun OwersList(modifier: Modifier = Modifier, users: List<Splitter>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(users) {
            Ower(modifier = modifier, user = it)
        }
    }
}

