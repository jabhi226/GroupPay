package com.example.grouppay.ui.features.addUser

import android.widget.Toast
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
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.features.groups.AutocompleteTextField
import com.example.grouppay.ui.theme.GroupPayTheme
import com.example.grouppay.ui.viewModel.GroupViewModel
import io.realm.kotlin.ext.copyFromRealm
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddParticipantScreen(navController: NavController = rememberNavController(), groupId: String?) {

    val viewModel: GroupViewModel = koinViewModel()
    val sug by viewModel.getAllParticipantsByText("").collectAsState(initial = emptyList())
    var participant by remember { mutableStateOf(Participant()) }
    val state = viewModel.saveResponse.collectAsState(initial = false)
    val context = LocalContext.current

    when (state.value) {
        true -> {
            navController.navigateUp()
        }

        false -> {}
    }

    GroupPayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    if (groupId == null) {
                        Toast.makeText(context, "group not found", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    viewModel.saveNewParticipantInTheGroup(groupId, participant)
                }) {
                    CommonText(text = "Save")
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
            },
        ) { innerPadding ->
            AutocompleteTextField(
                modifier = Modifier.padding(innerPadding),
                text = participant.name,
                updateText = {
                    participant = participant.copyFromRealm().apply {
                        name = it
                    }
                },
                suggestions = sug,
                getDisplayText = { it.name },
                selectSuggestion = {
                    participant = it.copyFromRealm()
                },
                saveNewSuggestion = {
                    participant = participant.copyFromRealm().apply {
                        name = it
                    }
                }
            )
        }
    }


}