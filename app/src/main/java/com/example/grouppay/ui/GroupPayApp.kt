package com.example.grouppay.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grouppay.ui.features.groups.AllGroupsScreen
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.AddContributionScreen
import com.example.grouppay.ui.features.addGroup.AddGroupScreen
import com.example.grouppay.ui.features.addUser.AddParticipantScreen
import com.example.grouppay.ui.features.groupDetails.GroupDetailsScreen
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.google.gson.Gson


@Composable
fun GroupPayApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "groups") {
        composable("groups") { AllGroupsScreen(navController) }
        composable("group_details/{group}") { navBackStackEntry ->
            val group = navBackStackEntry.arguments?.getString("group")
            GroupDetailsScreen(
                navController = navController,
                group = Gson().fromJson(group, GroupWithTotalExpense::class.java)
            )
        }
        composable("add_contribution") { AddContributionScreen(navController) }
        composable("add_groups") { AddGroupScreen(navController) }
        composable("add_participant/{group_id}") { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments?.getString("group_id")
            AddParticipantScreen(
                navController = navController,
                groupId = groupId
            )
        }
    }
}