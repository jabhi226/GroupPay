package com.example.grouppay.ui.features.addUser

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.ui.features.core.CommonOutlinedTextField

@Preview(showSystemUi = true)
@Composable
fun AddUserScreen(navController: NavController = rememberNavController()) {
    var splitterName by remember {
        mutableStateOf("")
    }

    Column {
        CommonOutlinedTextField(text = splitterName, hint = "Splitter name") {
            splitterName = it
        }


    }

}