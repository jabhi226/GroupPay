package com.example.grouppay.ui.features.groups.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grouppay.R
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.theme.GroupPayTheme

@Composable
fun SettlementScreen(
    navController: NavController,
    group: Group
) {

    GroupPayTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
//                    navController.navigate("add_participant/${group.id}")
                }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settlement),
                            contentDescription = "add_settlement"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Add Participant"
                        )
                    }
                }
            }) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // map(payer, (to be the payment maid, amount top be paid)
                // for each member find its pending payment mapping, for each payment
                // find all

                val limitedTransactions =
                    mutableMapOf<String, ArrayList<Pair<String, Double>>>()// ogpayerid, borroweid, amount

                for (member in group.participants) {
                    limitedTransactions[member.id] = ArrayList<Pair<String, Double>>().apply {
                        addAll(member.pendingPaymentsMapping.map {
                            Pair(it.originalPayerId, it.amountToBePaid)
                        })
                    }
                    for (payment in member.pendingPaymentsMapping) {
                        for (participant in group.participants) {
                            if (participant.id == payment.originalPayerId) {
                                // reduce the amount of originalPayerId for this member.id
                                limitedTransactions[participant.id]?.add(
                                    Pair(
                                        member.id,
                                        -payment.amountToBePaid
                                    )
                                )
                                continue
                            }
                        }
//                        println("===> $payment | ${group.participants.find { it.id== payment.originalPayerId }}")
//                        var item =
//                            limitedTransactions[member.id]?.find { it.first == payment.originalPayerId }
//                                ?: continue
//                        item = Pair(item.first, item.second - payment.amountToBePaid)
//                        limitedTransactions[member.id] = ArrayList<Pair<String, Double>>().apply {
//                            addAll(limitedTransactions[member.id]?.map {
//                                if (it.first == item.first) {
//                                    it.copy(second = it.second - payment.amountToBePaid)
//                                } else {
//                                    it
//                                }
//                            } ?: listOf())
//                        }
//                        if (limitedTransactions[member.id]?.first == payment.originalPayerId) {
//                            limitedTransactions[member.id] = limitedTransactions[member.id].first
//                        }
//                        limitedTransactions[payment.originalPayerId] =
//                            (limitedTransactions[payment.originalPayerId]
//                                ?: Pair(payment.originalPayerId, 0.0))
                    }
                }

                println("---> ${limitedTransactions}")

                val netBalanceMap = mutableMapOf<String, Double>()
                for (member in group.participants) {
                    println("--==> ${member.name} | ${member.amountOwedFromGroup} | ${member.amountBorrowedFromGroup}")
                    for (payment in member.pendingPaymentsMapping) {
                        netBalanceMap[member.id] =
                            (netBalanceMap[member.id] ?: 0.0) - payment.amountToBePaid
                        netBalanceMap[payment.originalPayerId] =
                            (netBalanceMap[payment.originalPayerId] ?: 0.0) + payment.amountToBePaid
                    }
                }
                println("==> $netBalanceMap")

                group.participants.forEach { participant ->
                    val payments = participant.pendingPaymentsMapping.groupBy { it.originalPayerId }
                    for (payment in payments) {
                        payment.key //originalPayerId
                        payment.value.map { payments1 -> group.participants.find { it.id == payments1.originalPayerId } }
                    }
                    CommonText(
                        modifier = Modifier.padding(8.dp),
                        text = participant.pendingPaymentsMapping.groupBy { it.originalPayerId }
                            .map { (key, value) ->
                                "${participant.name} will pay ₹ ${value.sumOf { it.amountToBePaid }} to ${value.first().originalPayerName} ($key)\n"
                            }.joinToString() + "\n\n"
                    )
                }
            }
        }
    }
}