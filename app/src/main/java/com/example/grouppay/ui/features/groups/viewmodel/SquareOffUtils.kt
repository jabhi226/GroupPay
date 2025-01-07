package com.example.grouppay.ui.features.groups.viewmodel

import com.example.grouppay.domain.Group

object SquareOffUtils {


    fun getSquareOffTransaction(group: Group): List<Triple<String, String, Double>> {
        println(
            group.participants.joinToString { groupMember ->
                "\n${groupMember.id} | ${groupMember.pendingPaymentsMapping.joinToString { "${it.originalPayerId}->${it.amountToBePaid}" }}"
            }
        )

        val squreOffValues = mutableMapOf<String, Double>()
        val toBePaidValues = mutableMapOf<String, Double>()
        val toBeReceivedValues = mutableMapOf<String, Double>()

        group.participants.map { groupMember ->
            val amountToBePaid = groupMember.pendingPaymentsMapping.sumOf { it.amountToBePaid }
            toBePaidValues[groupMember.id] = amountToBePaid
        }

        toBePaidValues.forEach { groupMember ->
            val amountToReceive =
                group.participants.filterNot { it.id == groupMember.key }.sumOf { member ->
                    member.pendingPaymentsMapping.filter { it.originalPayerId == groupMember.key }
                        .sumOf { it.amountToBePaid }
                }
            toBeReceivedValues[groupMember.key] = amountToReceive
            squreOffValues[groupMember.key] =
                amountToReceive - (toBePaidValues[groupMember.key] ?: 0.0)
        }

        return getMinimumTransactions(squreOffValues)
    }

    private fun getMinimumTransactions(
        squreOffValues: MutableMap<String, Double>
    ): List<Triple<String, String, Double>> {
        // Split participants into Debtors and Creditors
        val creditors = mutableListOf<Pair<String, Double>>()  // List of (id, amount owed)
        val debtors = mutableListOf<Pair<String, Double>>()  // List of (id, amount to pay)

        squreOffValues.forEach { (id, balance) ->
            if (balance > 0) {
                creditors.add(id to balance) // They need to receive money
            } else if (balance < 0) {
                debtors.add(id to -balance) // They need to pay money (negative balance, we store positive amount here)
            }
        }

        // Greedily match Debtors with Creditors
        val transactions =
            mutableListOf<Triple<String, String, Double>>()  // List of transactions (from, to, amount)

        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
            val creditor = creditors.first()
            val debtor = debtors.first()

            val paymentAmount = minOf(creditor.second, debtor.second)

            // Record the transaction (debtor pays creditor)
            transactions.add(Triple(debtor.first, creditor.first, paymentAmount))

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