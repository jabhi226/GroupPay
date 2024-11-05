package com.example.grouppay.ui.features.groupDetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.domain.Group


typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(
    val title: String,
    val icons: ImageVector,
    val screens: ComposableFun
) {
    data class ExpensesScreenItem(
        val navController: NavController,
        val group: Group
    ) : TabItem(
        title = "Expenses",
        icons = Icons.Default.Home,
        screens = { ExpensesScreen(group) })

    data class ParticipantScreen(
        val navController: NavController,
        val group: Group
    ) :
        TabItem(
            title = "Participants",
            icons = Icons.Default.ShoppingCart,
            screens = { ParticipantsScreen(navController, group) })
}

