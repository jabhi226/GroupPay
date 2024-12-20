package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.groups.view.screens.ExpensesScreen
import com.example.grouppay.ui.features.groups.view.screens.ParticipantsScreen

sealed class TabItem(
    val title: String,
    val icons: ImageVector,
    val screens: @Composable () -> Unit
) {
    data class ExpensesScreenItem(
        val navController: NavController,
        val group: Group
    ) : TabItem(
        title = "Expenses",
        icons = Icons.Default.Home,
        screens = { ExpensesScreen(navController, group) })

    data class ParticipantScreenItem(
        val navController: NavController,
        val group: Group
    ) :
        TabItem(
            title = "Participants",
            icons = Icons.Default.ShoppingCart,
            screens = { ParticipantsScreen(navController, group) })
}

