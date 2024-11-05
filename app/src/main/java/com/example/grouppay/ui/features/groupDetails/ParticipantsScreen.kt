package com.example.grouppay.ui.features.groupDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import com.google.gson.Gson


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantsScreen(
    navController: NavController,
    group: Group
) {

    //todo get participent list from db for being latest
    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate("add_participant/${group._id}")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_user),
                        contentDescription = "add_user"
                    )
                }
            },
            topBar = {
                TopAppBar(title = {
                    CommonText(
                        text = "Add User",
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
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    userScrollEnabled = true
                ) {
                    items(group.participants) {
                        ParticipantItem(participant = it)
                    }
                }
            }
        }
    }
}