package com.example.grouppay.ui.features.groups.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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


@Preview
@Composable
fun ParticipantItem(
    modifier: Modifier = Modifier,
    participant: GroupMember = Testing.getParticipent(),
    onGroupMemberClicked: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onGroupMemberClicked(participant.id)
            }
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.33F),
                text = participant.name,
                fontSize = 20.sp,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Paid",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 16.sp,
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
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Borrowed",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 16.sp,
                        text = "₹ ${participant.amountBorrowedFromGroup.roundToTwoDecimal()}",
                        textColor = if (participant.amountBorrowedFromGroup > 0.0) {
                            colorResource(R.color.amount_red)
                        } else if (participant.amountBorrowedFromGroup < 0.0) {
                            colorResource(R.color.amount_green)
                        } else {
                            MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                }
                /* todo @abhi
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Returned",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 18.sp,
                        text = "₹ ${participant.amountReturnedToOwner.roundToTwoDecimal()}",
                        textColor = MaterialTheme.colorScheme.inverseSurface
                    )
                }
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CommonText(
                        text = "Recived",
                        textColor = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CommonText(
                        fontSize = 18.sp,
                        text = "₹ ${participant.amountReceivedFromBorrower.roundToTwoDecimal()}",
                        textColor = MaterialTheme.colorScheme.inverseSurface
                    )
                }
                 */
//                AmountItem(title = "Paid", amount = participant.amountOwedFromGroup)
//                AmountItem(title = "Borrowed", amount = participant.amountBorrowedFromGroup)
//                AmountItem(title = "Returned", amount = participant.amountReturnedToOwner)

            }
        }
    }
}

@Composable
fun AmountItem(
    modifier: Modifier = Modifier,
    title: String = "Borrowed",
    amount: Double
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        CommonText(
            text = title,
            textColor = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(4.dp))
        CommonText(
            fontSize = 18.sp,
            text = "₹ ${amount.roundToTwoDecimal()}",
            textColor = if (amount > 0.0) {
                Color(0xFFFF5544)
            } else if (amount < 0.0) {
                Color(0xFF85BB65)
            } else {
                MaterialTheme.colorScheme.inverseSurface
            }
        )
    }
}