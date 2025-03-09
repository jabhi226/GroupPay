package com.example.grouppay.ui.features.groups.utils

import com.example.grouppay.domain.entities.Group
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel
import com.example.grouppay.ui.features.utils.roundToTwoDecimal

object SquareOffUtils {


    fun getSquareOffTransaction(groupMembers: List<GroupMember>): List<SquareOffTransactionModel> {
        val squreOffValues = mutableMapOf<GroupMember, Double>()
        val toBePaidValues = mutableMapOf<GroupMember, Double>()
        val toBeReceivedValues = mutableMapOf<GroupMember, Double>()

        groupMembers.map { groupMember ->
            toBePaidValues[groupMember] = groupMember.getAmountToBePaid()
        }

        toBePaidValues.forEach { groupMember ->
            val amountToReceive =
                groupMembers
                    .filterNot { it.id == groupMember.key.id }
                    .sumOf { member ->
                        member.pendingPaymentsMapping
                            .filter { it.originalPayerId == groupMember.key.id }
                            .sumOf { it.amountToBePaid }
                    }.roundToTwoDecimal()
            toBeReceivedValues[groupMember.key] = amountToReceive
            squreOffValues[groupMember.key] =
                amountToReceive - (toBePaidValues[groupMember.key] ?: 0.0)
        }

        return getMinimumTransactions(squreOffValues)
    }

    private fun getMinimumTransactions(
        squreOffValues: MutableMap<GroupMember, Double>
    ): List<SquareOffTransactionModel> {
        // Split participants into Debtors and Creditors
        val creditors = mutableListOf<Pair<GroupMember, Double>>()  // List of (id, amount owed)
        val debtors = mutableListOf<Pair<GroupMember, Double>>()  // List of (id, amount to pay)

        squreOffValues.forEach { (groupMember, balance) ->
            if (balance > 0) {
                creditors.add(groupMember to balance) // They need to receive money
            } else if (balance < 0) {
                debtors.add(groupMember to -balance) // They need to pay money (negative balance, we store positive amount here)
            }
        }

        // Greedily match Debtors with Creditors
        val transactions =
            mutableListOf<SquareOffTransactionModel>()  // List of transactions (from, to, amount)

        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
            val creditor = creditors.first()
            val debtor = debtors.first()

            val paymentAmount = minOf(creditor.second, debtor.second)

            // Record the transaction (debtor pays creditor)
            transactions.add(SquareOffTransactionModel(debtor.first, creditor.first, paymentAmount))

            // Update balances after payment
            if (creditor.second > paymentAmount) {
                creditors[0] = creditor.copy(second = creditor.second - paymentAmount)
            } else {
                creditors.removeAt(0)  // Credit is fully settled
            }

            if (debtor.second > paymentAmount) {
                debtors[0] = debtor.copy(second = debtor.second - paymentAmount)
            } else {
                debtors.removeAt(0)  // Debt is fully settled
            }
        }
        return transactions
    }
}