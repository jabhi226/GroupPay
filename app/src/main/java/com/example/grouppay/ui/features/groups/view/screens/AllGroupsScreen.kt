package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.R
import com.example.grouppay.ui.features.core.view.screen.EmptyScreen
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.Loading
import com.example.grouppay.ui.features.groups.view.components.GroupList
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.theme.GroupPayTheme
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun AllGroupsScreen(
    navController: NavController = rememberNavController()
) {

    val viewModel: GroupViewModel = koinViewModel()
    val groupListUiState by viewModel.groupListUiState.collectAsState(initial = AllGroupsScreenUiState.Loading)

    LaunchedEffect(Unit) {
        viewModel.getGroupList()
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 40.dp),
                onClick = { navController.navigate("add_groups") }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_group),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "add_group"
                    )
                    CommonText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = "Add Group",
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }, topBar = {
            CenterAlignedTopAppBar(title = {
                CommonText(
                    text = "All Groups",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            })
        }) { innerPadding ->
            when (groupListUiState) {
                is AllGroupsScreenUiState.Error -> {
                    EmptyScreen(
                        text = "No groups are created."
                    )
                }

                is AllGroupsScreenUiState.GroupList -> {
                    GroupList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        groups = (groupListUiState as? AllGroupsScreenUiState.GroupList)?.groupList ?: listOf()
                    ) {
                        navController.navigate("group_details/${Gson().toJson(it)}")
                    }
                }

                AllGroupsScreenUiState.Loading -> {
                    Loading()
                }
            }
        }
    }
}

sealed class AllGroupsScreenUiState {
    data object Loading : AllGroupsScreenUiState()
    data class Error(val message: String) : AllGroupsScreenUiState()
    data class GroupList(val groupList: List<GroupWithTotalExpense>) : AllGroupsScreenUiState()
}