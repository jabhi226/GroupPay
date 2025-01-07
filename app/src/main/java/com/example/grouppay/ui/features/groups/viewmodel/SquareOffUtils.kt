package com.example.grouppay.ui.features.groups.viewmodel

import com.example.grouppay.domain.Group
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.domain.PendingPayments
import com.example.grouppay.ui.features.groups.model.SquareOffTransactionModel

object SquareOffUtils {


    fun getSquareOffTransaction(
        group: Group = Group(
            id = "test",
            name = "test",
            participants = getParticipants()
        )
    ): List<SquareOffTransactionModel> {
        println(
            group.participants.joinToString { groupMember ->
                "\n${groupMember.id} | ${groupMember.pendingPaymentsMapping.joinToString { "${it.originalPayerId}->${it.amountToBePaid}" }}"
            }
        )

        val squreOffValues = mutableMapOf<GroupMember, Double>()
        val toBePaidValues = mutableMapOf<GroupMember, Double>()
        val toBeReceivedValues = mutableMapOf<GroupMember, Double>()

        group.participants.map { groupMember ->
            val amountToBePaid = groupMember.pendingPaymentsMapping.sumOf { it.amountToBePaid }
            toBePaidValues[groupMember] = amountToBePaid
        }

        toBePaidValues.forEach { groupMember ->
            val amountToReceive =
                group.participants.filterNot { it.id == groupMember.key.id }.sumOf { member ->
                    member.pendingPaymentsMapping.filter { it.originalPayerId == groupMember.key.id }
                        .sumOf { it.amountToBePaid }
                }
            toBeReceivedValues[groupMember.key] = amountToReceive
            squreOffValues[groupMember.key] =
                amountToReceive - (toBePaidValues[groupMember.key] ?: 0.0)
        }

        return getMinimumTransactions(squreOffValues)
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