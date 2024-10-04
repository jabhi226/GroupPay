package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.R
import com.example.grouppay.domain.Splitter
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.features.groups.getGroups
import com.example.grouppay.ui.theme.GroupPayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GroupDetailsScreen(navController: NavController = rememberNavController()) {
    val groupInfo by remember {
        mutableStateOf(getGroups()[0])
    }
    val contributions = remember {
        mutableStateListOf(*getContros().toTypedArray())
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_contribution") }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_payment),
                    contentDescription = "add_user"
                )
            }
        }, topBar = {
            CenterAlignedTopAppBar(title = {
                CommonText(
                    text = groupInfo.groupName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            })
        }) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(modifier = Modifier) {
                    items(items = contributions) {
                        ContributionItem(contribution = it)
                    }
                }
            }
        }
    }
}

fun getContros(): List<Contribution> {
    return (0..5).map {
        Contribution("PayerName", 300.0, getOwers())
    }
}

fun getOwers(): List<Splitter> {
    return (0..2).map {
        Splitter(it, "Ower jname", 100.0)
    }
}
