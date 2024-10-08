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
import com.example.grouppay.domain.Contribution
import com.example.grouppay.domain.GroupInfo
import com.example.grouppay.domain.Splitter
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GroupDetailsScreen(navController: NavController = rememberNavController()) {
    val groupInfo by remember {
        mutableStateOf(GroupInfo().apply {
            groupName = "Test"
            contributions = getContros()
            groupMembers = groupMembers()
        })
    }
    groupInfo.contributions.map {
        it.group = groupInfo
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
                    items(items = groupInfo.contributions) {
                        ContributionItem(contribution = it)
                    }
                }
            }
        }
    }
}

fun getContros(): RealmList<Contribution> {
    return (0..5).map {
        Contribution().apply {
            paidBy = groupMembers()[0]
            remainingSplitters = groupMembers().drop(1).toRealmList()
        }
    }.toRealmList()
}

fun groupMembers(): RealmList<Splitter> {
    return (0..2).map {
        Splitter().apply {
            userName = "Test"
            amountOwed = 0.0
        }
    }.toRealmList()
}
