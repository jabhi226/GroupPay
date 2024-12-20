package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grouppay.domain.Participant

@Composable
fun OwersList(modifier: Modifier = Modifier, users: List<Participant>) {
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

