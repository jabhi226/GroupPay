package com.example.grouppay.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grouppay.ui.features.groups.AllGroupsScreen
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.AddContributionScreen
import com.example.grouppay.ui.features.addGroup.AddGroupScreen
import com.example.grouppay.ui.features.addUser.AddUserScreen
import com.example.grouppay.ui.features.groupDetails.GroupDetailsScreen


@Composable
fun GroupPayApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "groups") {
        composable("groups") { AllGroupsScreen(navController) }
        composable("group_details") { GroupDetailsScreen(navController) }
        composable("add_contribution") { AddContributionScreen(navController) }
        composable("add_groups") { AddGroupScreen(navController) }
        composable("add_user") { AddUserScreen(navController) }
    }
}