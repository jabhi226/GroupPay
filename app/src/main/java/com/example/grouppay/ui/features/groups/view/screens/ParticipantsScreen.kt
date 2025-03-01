package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import com.example.grouppay.ui.features.core.view.components.EmptyScreen
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ParticipantsScreen(
    navController: NavController,
    group: Group
) {

    val viewModel: GroupViewModel = koinViewModel()
    val groupInfo by viewModel.groupInfoFlow.collectAsState()

    LaunchedEffect(group.id) {
        viewModel.getGroupInformationFlow(group.id)
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
                        items(groupInfo?.participants ?: listOf()) {
                            ParticipantItem(participant = it)
                        }
                        item { Spacer(modifier = Modifier.padding(40.dp)) }
                    }
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
                        text = "₹ ${participant.amountOwedFromGroup.roundToTwoDecimal()}",
                        textColor = if (participant.amountOwedFromGroup > 0.0) {
                            colorResource(R.color.amount_green)
                        } else if (participant.amountOwedFromGroup < 0.0) {
                            colorResource(R.color.amount_red)
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Borrowed",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 18.sp,
                        text = "₹ ${participant.amountBorrowedFromGroup.roundToTwoDecimal()}",
                        textColor = if (participant.amountBorrowedFromGroup > 0.0) {
                            Color(0xFFFF5544)
                        } else if (participant.amountBorrowedFromGroup < 0.0) {
                            Color(0xFF85BB65)
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
            }
        }
    }
}