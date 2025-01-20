package com.example.grouppay

import com.example.grouppay.domain.ExpenseMember
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.domain.PendingPayments
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.utils.formatToTwoDecimalPlaces
import org.junit.Test

import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        SquareOffUtils.getSquareOffTransaction(
//            Group(
//                id = "test",
//                name = "test",
//                participants = getParticipants()
//            )
//        )
        val total = "300"
//        getTotal(total, Testing.groupExpenseMembers()[0], Testing.groupExpenseMembers())
//        getTotal(total, Testing.groupExpenseMembers()[1], Testing.groupExpenseMembers())
//        getTotal(total, Testing.groupExpenseMembers()[2], Testing.groupExpenseMembers())

//        getPercentage("33.33", total)
        getAmount("10", total)

    }

    private fun getPercentage(percentageText: String, totalAmountPaid: String) {
        try {
            var amountText = "0"
            val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
            val amount = if (total == 0.0) {
                "0"
            } else {
                ((total * percentageText.toDouble()) / 100).formatToTwoDecimalPlaces()
            }
            println("======> $amount")
            if (amountText.toDouble() != amount.toDouble()) {
                amountText = amount
            }
            println("======> $amountText")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAmount(amountText: String, totalAmountPaid: String) {

        try {
            var percentageText = "33.33"
            val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
            val percentage = if (total == 0.0) {
                "0"
            } else {
                ((amountText.toDouble() / total) * 100).formatToTwoDecimalPlaces()
            }

            if (percentage.toDouble() != percentageText.toDouble()) {
                percentageText = percentage
            }
            println("=====> $percentageText")
//            updateParticipantAmount(participant.apply {
//                this.setAmountBorrowedForExpense(amountText)
//            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getTotal(
        totalAmountPaid: String,
        participant: ExpenseMember,
        totalSelectedParticipants: List<ExpenseMember>,
    ) {
        val total = totalAmountPaid.toDoubleOrNull() ?: 0.0
        val totalParticipants = totalSelectedParticipants.size
        val isSelected = participant.isSelected

        var amountText = ""
        var percentageText = ""
        // Avoid dividing by zero
        if (total == 0.0 || totalParticipants == 0) {
            amountText = "0"
            percentageText = "0"
            return
        }

        // Calculate the distributed amounts and percentages
        val distributedAmount = (total / totalParticipants).formatToTwoDecimalPlaces().toDouble()
        val distributedPercentage =
            ((distributedAmount / total) * 100).formatToTwoDecimalPlaces().toDouble()

        // If the current participant is the last one, calculate their final share
        val (amt, per) = if (isSelected) {
            if (participant.id == totalSelectedParticipants.last().id) {
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

        // Update amount and percentage texts
        amountText = amt
        percentageText = per

        // For debugging purposes (optional)
        println("===> Participant ID: ${participant.id} | isSelected: $isSelected | Amount: $amt | Percentage: $per")
    }

    private fun getParticipants(): List<GroupMember> {
        val list = ArrayList<GroupMember>()
        val personA = "ABC"
        val personB = "XYZ"
        val personC = "PQR"
        list.add(GroupMember(
            id = personA,
            name = personA,
            pendingPaymentsMapping = ArrayList<PendingPayments>().apply {
                add(
                    PendingPayments(
                        personB,
                        personB,
                        500.0
                    )
                )
                add(
                    PendingPayments(
                        personB,
                        personB,
                        100.0
                    )
                )
                add(
                    PendingPayments(
                        personC,
                        personC,
                        70.0
                    )
                )
            }
        ))
        list.add(GroupMember(
            id = personB,
            name = personB,
            pendingPaymentsMapping = ArrayList<PendingPayments>().apply {
                add(
                    PendingPayments(
                        personA,
                        personA,
                        200.0
                    )
                )
                add(
                    PendingPayments(
                        personA,
                        personA,
                        30.0
                    )
                )
                add(
                    PendingPayments(
                        personC,
                        personC,
                        10.0
                    )
                )
                add(
                    PendingPayments(
                        personA,
                        personA,
                        500.0
                    )
                )
                add(
                    PendingPayments(
                        personC,
                        personC,
                        10.0
                    )
                )
            }
        ))
        list.add(GroupMember(
            id = personC,
            name = personC,
            pendingPaymentsMapping = ArrayList<PendingPayments>().apply {
                add(
                    PendingPayments(
                        personA,
                        personA,
                        200.0
                    )
                )
                add(
                    PendingPayments(
                        personA,
                        personA,
                        30.0
                    )
                )

                add(
                    PendingPayments(
                        personB,
                        personB,
                        20.0
                    )
                )
                add(
                    PendingPayments(
                        personA,
                        personA,
                        500.0
                    )
                )
            }
        ))
        return list
    }

    private fun getRandomParticipants(): List<GroupMember> {
        return (0..5).map {
            GroupMember(
                id = it.toString(),
                name = it.toString(),
                pendingPaymentsMapping = getPendingPaymentMapping(it.toString())
            )
        }
    }

    private fun getPendingPaymentMapping(borrowerId: String): ArrayList<PendingPayments> {
        val list = ArrayList<PendingPayments>()
        (0..5).forEach { _ ->
            while (true) {
                val ogPayerId = getRandomNumber()
                if (ogPayerId != borrowerId) {
                    list.add(PendingPayments(ogPayerId, ogPayerId, getRandomAmount()))
                    break
                }
            }
        }
        return list
    }

    private fun getRandomNumber(): String {
        return Random.nextInt(6).toString()
    }

    private fun getRandomAmount(): Double {
        return Random.nextInt(100, 200).toDouble()
    }

}