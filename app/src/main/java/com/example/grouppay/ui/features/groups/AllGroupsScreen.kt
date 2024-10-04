package com.example.grouppay.ui.features.groups

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.R
import com.example.grouppay.domain.GroupInfo
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import com.example.grouppay.ui.viewModel.GroupViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun AllGroupsScreen(navController: NavController = rememberNavController()) {

    val viewModel: GroupViewModel = koinViewModel()
    val groups by viewModel.groupList.collectAsState(initial = emptyList())

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.saveNewGroup()
//                navController.navigate("add_groups")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_group),
                    contentDescription = "add_group"
                )
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
            GroupList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), groups = groups
            ) {
                navController.navigate("group_details")
            }
        }
    }
}
