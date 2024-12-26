package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme


@Composable
fun ParticipantsScreen(
    navController: NavController,
    group: Group
) {

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate("add_participant/${group.id}")
                }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_user),
                            contentDescription = "add_user"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Add Participant"
                        )
                    }
                }
            }) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    userScrollEnabled = true
                ) {
                    items(group.participants) {
                        ParticipantItem(participant = it)
                    }
                    item { Spacer(modifier = Modifier.padding(40.dp)) }
                }
            }
        }
    }
}


@Preview
@Composable
fun ParticipantItem(
    modifier: Modifier = Modifier,
    participant: GroupMember = Testing.getParticipent()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = participant.name,
                fontSize = 22.sp,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Row {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Paid",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 18.sp,
                        text = "₹ ${participant.amountOwedFromGroup}",
                        textColor = MaterialTheme.colorScheme.inverseSurface
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Owes",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 18.sp,
                        text = "₹ ${participant.amountBorrowedFromGroup}",
                        textColor = if (participant.amountBorrowedFromGroup > 0.0) {
                            Color.Green
                        } else if (participant.amountBorrowedFromGroup < 0.0) {
                            Color.Red
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
            }
        }
    }
}