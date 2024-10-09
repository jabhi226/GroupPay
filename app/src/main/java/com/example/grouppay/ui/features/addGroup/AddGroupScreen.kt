package com.example.grouppay.ui.features.addGroup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.core.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme
import com.example.grouppay.ui.viewModel.GroupViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun AddGroupScreen(navController: NavController = rememberNavController()) {

    val viewModel: GroupViewModel = koinViewModel()
    val group by remember {
        mutableStateOf(Group())
    }

    GroupPayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.saveNewGroup(group)
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
                    text = group.name,
                    hint = "Group name"
                ) {
                    group.name = it
                }
            }
        }
    }

}