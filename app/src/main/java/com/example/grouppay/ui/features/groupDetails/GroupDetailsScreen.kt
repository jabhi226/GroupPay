package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.R
import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense
import com.example.grouppay.ui.theme.GroupPayTheme
import com.example.grouppay.ui.viewModel.GroupViewModel
import com.google.gson.Gson
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    navController: NavController = rememberNavController(),
    group: GroupWithTotalExpense
) {

    val viewModel = koinViewModel<GroupViewModel>()
    val groupInfo by remember {
        mutableStateOf(viewModel.getGroupInformation(group._id))
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
            TopAppBar(title = {
                CommonText(
                    text = group.groupName,
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
                    items(items = groupInfo.expenses) {
                        ContributionItem(contribution = it)
                    }
                }
            }
        }
    }
}

fun getContros(): RealmList<Expense> {
    return (0..5).map {
        Expense().apply {
            paidBy = groupMembers()[0]
            remainingParticipants = groupMembers().drop(1).toRealmList()
        }
    }.toRealmList()
}

fun groupMembers(): RealmList<Participant> {
    return (0..2).map {
        Participant().apply {
            name = "Test"
            amountOwedFromGroup = 0.0
        }
    }.toRealmList()
}
