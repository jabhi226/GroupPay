package com.example.grouppay.ui.features.addParticipant.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.features.addParticipant.viewModel.AddParticipantViewModel
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.AutocompleteTextField
import com.example.grouppay.ui.theme.GroupPayTheme
import io.realm.kotlin.ext.copyFromRealm
import org.koin.androidx.compose.koinViewModel
import org.mongodb.kbson.BsonObjectId
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddParticipantScreen(navController: NavController = rememberNavController(), groupId: String?) {

    val viewModel: AddParticipantViewModel = koinViewModel()
    val suggestions by viewModel.getAllParticipantsByText("").collectAsState(initial = emptyList())
    var participant by remember { mutableStateOf("") }
    val state = viewModel.saveResponse.collectAsState()
    val context = LocalContext.current

    when (state.value) {
        true -> {
            Toast.makeText(context, "Participant $participant added.", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }

        false -> {
            Toast.makeText(context, "Error adding participant $participant.", Toast.LENGTH_SHORT)
                .show()
        }

        else -> {}
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
                    Log.d("######", "groupId: ${groupId}")
                    viewModel.saveNewParticipantInTheGroup(
                        groupId,
                        Participant().apply {
                            name = participant
                        })
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
                AutocompleteTextField(
                    text = participant,
                    hint = "Participant name",
                    updateText = {
                        participant = it
                    },
                    suggestions = suggestions,
                    selectSuggestion = {
                        participant = it.name
                    },
                    saveNewSuggestion = {
                        participant = it
                    }
                )
            }

        }
    }


}