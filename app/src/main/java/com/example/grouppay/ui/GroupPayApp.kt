package com.example.grouppay.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grouppay.ui.features.groups.view.screens.AllGroupsScreen
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.addExpense.view.AddExpenseScreen
import com.example.grouppay.ui.features.addGroup.view.AddGroupScreen
import com.example.grouppay.ui.features.participantDetails.view.ParticipantDetailsScreen
import com.example.grouppay.ui.features.groups.view.screens.GroupDetailsScreen
import com.example.grouppay.domain.entities.GroupWithTotalExpense
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

        composable("add_groups") { AddGroupScreen(navController) }

        composable("add_participant/{group_id}") { navBackStackEntry ->
            NavigateToPatientDetailsPage(navBackStackEntry, navController)
        }

        composable("add_participant/{group_id}/{participant_id}") { navBackStackEntry ->
            NavigateToPatientDetailsPage(navBackStackEntry, navController)
        }

        composable("add_expense/{group_id}") { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments?.getString("group_id")
            AddExpenseScreen(
                navController = navController,
                groupId = groupId
            )
        }
    }

}

@Composable
fun NavigateToPatientDetailsPage(
    navBackStackEntry: NavBackStackEntry,
    navController: NavHostController
) {
    val groupId = navBackStackEntry.arguments?.getString("group_id")
    val participantId = navBackStackEntry.arguments?.getString("participant_id")
    ParticipantDetailsScreen(
        navController = navController,
        groupId = groupId,
        participantId = participantId
    )
}
