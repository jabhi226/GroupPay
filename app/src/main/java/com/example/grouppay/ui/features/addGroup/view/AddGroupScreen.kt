package com.example.grouppay.ui.features.addGroup.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.addGroup.viewModel.AddGroupViewModel
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddGroupScreen(navController: NavController = rememberNavController()) {

    val viewModel: AddGroupViewModel = koinViewModel()
    var text by remember { mutableStateOf("") }
    val state = viewModel.saveResponse.collectAsState(initial = false)

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
                    viewModel.saveNewGroup(text)
                }) {
                    CommonText(text = "Save")
                }
            },
            topBar = {
                TopAppBar(title = {
                    CommonText(
                        text = "Add Group",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                })
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                CommonOutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    text = text,
                    hint = "Group name"
                ) {
                    text = it
                }
            }
        }
    }
}