package com.example.grouppay.ui.features.addExpense.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.grouppay.domain.entities.ExpenseMember
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.utils.formatToTwoDecimalPlaces
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun ParticipantContributions(
    modifier: Modifier = Modifier,
    participant: ExpenseMember,
    index: Int,
    totalAmountPaid: String,
    totalSelectedParticipants: List<ExpenseMember>,
    keyboardActions: KeyboardActions,
    updateParticipantAmount: (ExpenseMember) -> Unit = {},
    updateParticipantSelection: (ExpenseMember) -> Unit = {}
) {
    var amountText by remember { mutableStateOf("") }
    var percentageText by remember { mutableStateOf("") }
    val isSelected by rememberUpdatedState(participant.isSelected)
    var isCalculatingAmount by remember { mutableStateOf(false) }
    var isCalculatingPercentage by remember { mutableStateOf(false) }

    LaunchedEffect(totalAmountPaid, totalSelectedParticipants, participant) {
        isCalculatingAmount = true
        isCalculatingPercentage = true
        val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
        val totalParticipants = totalSelectedParticipants.size

        if (total == 0.0 || totalParticipants == 0) {
            amountText = "0"
            percentageText = "0"
            return@LaunchedEffect
        }

        val distributedAmount = (total / totalParticipants).formatToTwoDecimalPlaces().toDouble()
        val distributedPercentage =
            ((distributedAmount / total) * 100).formatToTwoDecimalPlaces().toDouble()

        val (amt, per) = if (isSelected) {
            if ((totalSelectedParticipants.count { it.isSelected } > 0) && (participant.groupMemberId == totalSelectedParticipants.last { it.isSelected }.groupMemberId)) {
                val distributedAmountExceptLast = distributedAmount * (totalParticipants - 1)
                val distributedPercentageExceptLast =
                    distributedPercentage * (totalParticipants - 1)

                val lastAmount = total - distributedAmountExceptLast
                val lastPercentage = 100 - distributedPercentageExceptLast
                Pair(
                    lastAmount.formatToTwoDecimalPlaces(),
                    lastPercentage.formatToTwoDecimalPlaces()
                )
            } else {
                Pair(
                    distributedAmount.formatToTwoDecimalPlaces(),
                    distributedPercentage.formatToTwoDecimalPlaces()
                )
            }
        } else {
            Pair("0", "0")
        }

        if (amountText != amt) {
            amountText = amt
        }
        if (percentageText != per) {
            percentageText = per
        }
        updateParticipantAmount(participant.apply {
            this.setAmountBorrowedForExpense(amountText)
        })
    }

    LaunchedEffect(percentageText, totalAmountPaid) {
        if (isCalculatingPercentage) {
            delay(300)
            isCalculatingPercentage = false
            return@LaunchedEffect
        }
        try {
            val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
            val amount = if (total == 0.0 || !participant.isSelected) {
                "0"
            } else {
                val calculatedAmount = BigDecimal(total * percentageText.toDouble())
                    .divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
                    .toString()

                calculatedAmount
            }
            if (amountText.toDouble() != amount.toDouble()) {
                amountText = amount
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(amountText, totalAmountPaid) {
        if (isCalculatingAmount) {
            delay(300)
            isCalculatingAmount = false
            return@LaunchedEffect
        }
        try {
            val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
            val percentage = if (total == 0.0 || !participant.isSelected) {
                "0"
            } else {
                ((amountText.toDouble() / total) * 100).formatToTwoDecimalPlaces()
            }

            if (percentage.toDouble() != percentageText.toDouble()) {
                percentageText = percentage
            }
            updateParticipantAmount(participant.apply {
                this.setAmountBorrowedForExpense(amountText)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(participant.amountBorrowedForExpense) {
        amountText = participant.amountBorrowedForExpense.toString().replace(".0", "")
    }

    fun updateAmount(s: String) {
        amountText = s.replace(".0", "")
    }

    fun updatePercentage(s: String) {
        percentageText = s.replace(".0", "")
    }

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CommonText(
                text = "${index + 1}: ${participant.name}",
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = {
                    updateParticipantSelection(participant)
                },
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight(1F)
            ) {
                CommonOutlinedTextField(
                    modifier = modifier,
                    text = amountText,
                    hint = "Amount in ₹",
                    updateText = { updateAmount(it) },
                    maxCharacterLength = 6,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = if (index == totalSelectedParticipants.size - 1) ImeAction.Done else ImeAction.Next,
                        keyboardType = KeyboardType.Number,
                    ),
                    keyboardActions = keyboardActions,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.weight(1F)
            ) {
                CommonOutlinedTextField(
                    modifier = Modifier,
                    text = percentageText,
                    hint = "Amount in %",
                    updateText = {
                        updatePercentage(it)
                    },
                    maxCharacterLength = 6,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(
                        onDone = {
                            updateParticipantAmount(participant.apply {
                                this.setAmountBorrowedForExpense(amountText)
                            })
                        }
                    )
                )
            }
        }
    }
}