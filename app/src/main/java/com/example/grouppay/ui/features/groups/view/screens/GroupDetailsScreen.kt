package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.view.components.TabItem
import com.example.grouppay.domain.entities.GroupWithTotalExpense
import com.example.grouppay.ui.theme.GroupPayTheme
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    navController: NavController,
    group: GroupWithTotalExpense
) {
    val viewModel = koinViewModel<GroupViewModel>()
    val groupInfo by viewModel.groupInfo.collectAsState()

    LaunchedEffect(group.id) {
        viewModel.getGroupInformation(group.id)
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        CommonText(
                            text = group.groupName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {

                    }
                )
            }) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                groupInfo?.let { GroupDetailTabs(navController, it) }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GroupDetailTabs(
    navController: NavController = rememberNavController(),
    groupInfo: Group
) {
    val tabs = listOf(
        TabItem.ExpensesScreenItem(navController, groupInfo),
        TabItem.ParticipantScreenItem(navController, groupInfo),
        TabItem.SettlementScreenItem(navController, groupInfo),
    )

    val pagerState = com.google.accompanist.pager.rememberPagerState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Tabs(tabs = tabs, pagerState = pagerState)
        TabContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier
            .fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        indicator = { _ ->
            Box {}
        },
    ) {
        tabs.forEachIndexed { index, tabItem ->
            val isSelected = pagerState.currentPage == index
            LeadingIconTab(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.background
                        }
                    ),
                selected = isSelected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    CommonText(
                        text = tabItem.title, textColor = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                },
                icon = { },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.Blue,
                enabled = true
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screens()
    }
}