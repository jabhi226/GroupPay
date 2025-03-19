package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.entities.Group
import com.example.grouppay.ui.features.core.view.screen.EmptyScreen
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.view.components.ParticipantItem
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ParticipantsScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: GroupViewModel = koinViewModel()
    val groupInfo by viewModel.groupInfo.collectAsState()

    LaunchedEffect(group.id) {
        viewModel.getGroupInformation(group.id)
    }

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate("add_participant/${group.id}")
                }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_user),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "add_user"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Add Member",
                            textColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }) { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (groupInfo?.participants?.isEmpty() == true) {
                    EmptyScreen(
                        text = "No members are found with ${group.name} group."
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                        userScrollEnabled = true
                    ) {
                        items(groupInfo?.participants ?: listOf()) { groupMember ->
                            ParticipantItem(participant = groupMember) {
                                navController.navigate("add_participant/${group.id}/${it}")
                            }
                        }
                        item { Spacer(modifier = Modifier.padding(40.dp)) }
                    }
                }
            }
        }
    }
}