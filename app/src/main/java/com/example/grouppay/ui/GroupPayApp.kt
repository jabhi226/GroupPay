package com.example.grouppay.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grouppay.ui.features.groups.view.screens.AllGroupsScreen
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.addExpense.view.AddExpenseScreen
import com.example.grouppay.ui.features.addGroup.view.AddGroupScreen
import com.example.grouppay.ui.features.addParticipant.view.AddParticipantScreen
import com.example.grouppay.ui.features.groups.view.screens.GroupDetailsScreen
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.google.gson.Gson
import org.bson.types.ObjectId


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
            val groupId = navBackStackEntry.arguments?.getString("group_id")
            val groupId1 = try {
                ObjectId(groupId)
            } catch (e: IllegalArgumentException) {
                null
            }
            AddParticipantScreen(
                navController = navController,
                groupId = groupId1?.toHexString() ?: groupId
            )
        }

        composable("add_expense/{group_id}") { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments?.getString("group_id")
            AddExpenseScreen(groupId = groupId)
        }
    }
}
