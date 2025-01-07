package com.example.grouppay

import com.example.grouppay.domain.Group
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.domain.PendingPayments
import com.example.grouppay.ui.features.groups.viewmodel.SquareOffUtils
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
        SquareOffUtils.getSquareOffTransaction(Group(id = "test", name = "test", participants = getParticipants()))
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