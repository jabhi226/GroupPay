package com.example.grouppay.ui.features.addParticipant.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.ui.features.addParticipant.viewModel.AddParticipantViewModel
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.utils.showToast
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddParticipantScreen(navController: NavController = rememberNavController(), groupId: String?) {

    val viewModel: AddParticipantViewModel = koinViewModel()
    var participant by remember { mutableStateOf("") }
    val state by viewModel.saveResponse.collectAsState(null)
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            true -> {
                context.showToast("Participant $participant added.")
                navController.navigateUp()
            }

            false -> {
                context.showToast("Error adding participant $participant.")
            }

            else -> {}
        }

    }

    GroupPayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    if (groupId == null) {
                        Toast.makeText(context, "Group not found", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    viewModel.saveNewParticipantInTheGroup(
                        groupId,
                        GroupMember(name = participant)
                    )
                }) {
                    CommonText(text = "Save")
                }
            },
            topBar = {
                TopAppBar(title = {
                    CommonText(
                        text = "Add Participant",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                CommonOutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    text = participant,
                    hint = "Participant name",
                    updateText = {
                        participant = it
                    }
                )
            }

        }
    }


}