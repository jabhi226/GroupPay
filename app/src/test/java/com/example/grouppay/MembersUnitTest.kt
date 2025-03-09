package com.example.grouppay

import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.domain.entities.Expense
import com.example.grouppay.domain.entities.PendingPayments
import com.example.grouppay.ui.features.utils.roundToTwoDecimal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.kotlin.ext.query
import org.junit.Test
import org.mongodb.kbson.BsonObjectId.Companion.invoke


class MembersUnitTest {

    @Test
    fun testGetInformation() {
        getInformation()
    }

    /**
     *
     *  [{"dateOfExpense":1741461417045,"groupId":"67cc9626a35f926ec74434dd","id":"67cc97a9b33f786311ed41c8","isSquareOff":false,"label":"Lassi","paidBy":{"amountBorrowedForExpense":50.0,"amountOwedForExpense":80.0,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97a9b33f786311ed41ca","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":50.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc97a9b33f786311ed41c8","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97a9b33f786311ed41cc","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":30.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc97a9b33f786311ed41c8","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc97a9b33f786311ed41ce","isSelected":true,"name":"Suraj"}],"totalAmountPaid":80.0},{"dateOfExpense":1741461488829,"groupId":"67cc9626a35f926ec74434dd","id":"67cc97f0b33f786311ed41eb","isSquareOff":false,"label":"Lunch","paidBy":{"amountBorrowedForExpense":120.0,"amountOwedForExpense":2.0,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97f0b33f786311ed41ed","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":120.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc97f0b33f786311ed41eb","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97f0b33f786311ed41ef","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":100.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc97f0b33f786311ed41eb","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc97f0b33f786311ed41f1","isSelected":true,"name":"Suraj"}],"totalAmountPaid":220.0},{"dateOfExpense":1741461828817,"groupId":"67cc9626a35f926ec74434dd","id":"67cc99447a52d73f946fd041","isSquareOff":false,"label":"franky","paidBy":{"amountBorrowedForExpense":70.0,"amountOwedForExpense":1.0,"groupExpenseId":"","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc99447a52d73f946fd043","isSelected":true,"name":"Suraj"},"remainingParticipants":[{"amountBorrowedForExpense":100.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc99447a52d73f946fd041","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc99447a52d73f946fd045","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":70.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc99447a52d73f946fd041","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc99447a52d73f946fd047","isSelected":true,"name":"Suraj"}],"totalAmountPaid":170.0},{"dateOfExpense":1741462124792,"groupId":"67cc9626a35f926ec74434dd","id":"67cc9a6c329a9361c275257b","isSquareOff":false,"label":"momos","paidBy":{"amountBorrowedForExpense":100.0,"amountOwedForExpense":2.0,"groupExpenseId":"","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9a6c329a9361c275257d","isSelected":true,"name":"Suraj"},"remainingParticipants":[{"amountBorrowedForExpense":100.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc9a6c329a9361c275257b","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9a6c329a9361c275257f","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":100.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc9a6c329a9361c275257b","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9a6c329a9361c2752581","isSelected":true,"name":"Suraj"}],"totalAmountPaid":200.0},{"dateOfExpense":1741462938575,"groupId":"67cc9626a35f926ec74434dd","id":"67cc9d9ab8d6537fde4769d6","isSquareOff":false,"label":"test","paidBy":{"amountBorrowedForExpense":120.0,"amountOwedForExpense":5.0,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9d9ab8d6537fde4769d8","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":120.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc9d9ab8d6537fde4769d6","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9d9ab8d6537fde4769da","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":80.0,"amountOwedForExpense":0.0,"groupExpenseId":"67cc9d9ab8d6537fde4769d6","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9d9ab8d6537fde4769dc","isSelected":true,"name"
     */
    private fun getInformation() {

        val participants = ArrayList<GroupMember>()
        val participantsListGsonString =
            """[{"amountBorrowedFromGroup":0.0,"amountOwedFromGroup":0.0,"amountReceivedFromBorrower":0.0,"amountReturnedToOwner":0.0,"id":"67cc9774b33f786311ed41a5","name":"Abhishek","pendingPaymentsMapping":[],"profilePictureUriPath":"content://media/external/images/media/1000000020"},{"amountBorrowedFromGroup":0.0,"amountOwedFromGroup":0.0,"amountReceivedFromBorrower":0.0,"amountReturnedToOwner":0.0,"id":"67cc9787b33f786311ed41ae","name":"Suraj","pendingPaymentsMapping":[],"profilePictureUriPath":"content://media/external/images/media/1000000021"}]"""
        val participantsListGson =
            Gson().fromJson(participantsListGsonString, object : TypeToken<List<GroupMember>>() {})
        participants.addAll(participantsListGson)
        val expenses = ArrayList<Expense>()
        val expensesGsonString =
            """[{"dateOfExpense":1741461417045,"groupId":"67cc9626a35f926ec74434dd","id":"67cc97a9b33f786311ed41c8","isSquareOff":false,"label":"Lassi","paidBy":{"amountBorrowedForExpense":50,"amountOwedForExpense":80,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97a9b33f786311ed41ca","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":50,"amountOwedForExpense":0,"groupExpenseId":"67cc97a9b33f786311ed41c8","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97a9b33f786311ed41cc","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":30,"amountOwedForExpense":0,"groupExpenseId":"67cc97a9b33f786311ed41c8","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc97a9b33f786311ed41ce","isSelected":true,"name":"Suraj"}],"totalAmountPaid":80},{"dateOfExpense":1741461488829,"groupId":"67cc9626a35f926ec74434dd","id":"67cc97f0b33f786311ed41eb","isSquareOff":false,"label":"Lunch","paidBy":{"amountBorrowedForExpense":120,"amountOwedForExpense":2,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97f0b33f786311ed41ed","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":120,"amountOwedForExpense":0,"groupExpenseId":"67cc97f0b33f786311ed41eb","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc97f0b33f786311ed41ef","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":100,"amountOwedForExpense":0,"groupExpenseId":"67cc97f0b33f786311ed41eb","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc97f0b33f786311ed41f1","isSelected":true,"name":"Suraj"}],"totalAmountPaid":220},{"dateOfExpense":1741461828817,"groupId":"67cc9626a35f926ec74434dd","id":"67cc99447a52d73f946fd041","isSquareOff":false,"label":"franky","paidBy":{"amountBorrowedForExpense":70,"amountOwedForExpense":1,"groupExpenseId":"","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc99447a52d73f946fd043","isSelected":true,"name":"Suraj"},"remainingParticipants":[{"amountBorrowedForExpense":100,"amountOwedForExpense":0,"groupExpenseId":"67cc99447a52d73f946fd041","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc99447a52d73f946fd045","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":70,"amountOwedForExpense":0,"groupExpenseId":"67cc99447a52d73f946fd041","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc99447a52d73f946fd047","isSelected":true,"name":"Suraj"}],"totalAmountPaid":170},{"dateOfExpense":1741462124792,"groupId":"67cc9626a35f926ec74434dd","id":"67cc9a6c329a9361c275257b","isSquareOff":false,"label":"momos","paidBy":{"amountBorrowedForExpense":100,"amountOwedForExpense":2,"groupExpenseId":"","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9a6c329a9361c275257d","isSelected":true,"name":"Suraj"},"remainingParticipants":[{"amountBorrowedForExpense":100,"amountOwedForExpense":0,"groupExpenseId":"67cc9a6c329a9361c275257b","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9a6c329a9361c275257f","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":100,"amountOwedForExpense":0,"groupExpenseId":"67cc9a6c329a9361c275257b","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9a6c329a9361c2752581","isSelected":true,"name":"Suraj"}],"totalAmountPaid":200},{"dateOfExpense":1741462938575,"groupId":"67cc9626a35f926ec74434dd","id":"67cc9d9ab8d6537fde4769d6","isSquareOff":false,"label":"test","paidBy":{"amountBorrowedForExpense":120,"amountOwedForExpense":5,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9d9ab8d6537fde4769d8","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":120,"amountOwedForExpense":0,"groupExpenseId":"67cc9d9ab8d6537fde4769d6","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cc9d9ab8d6537fde4769da","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":80,"amountOwedForExpense":0,"groupExpenseId":"67cc9d9ab8d6537fde4769d6","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cc9d9ab8d6537fde4769dc","isSelected":true,"name":"Suraj"}]},{"dateOfExpense":1741545457761,"groupId":"67cc9626a35f926ec74434dd","id":"67cddff1bee2f0736f7c2f80","isSquareOff":false,"label":"test","paidBy":{"amountBorrowedForExpense":120,"amountOwedForExpense":200,"groupExpenseId":"","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cddff1bee2f0736f7c2f82","isSelected":true,"name":"Abhishek"},"remainingParticipants":[{"amountBorrowedForExpense":120,"amountOwedForExpense":0,"groupExpenseId":"67cddff1bee2f0736f7c2f80","groupMemberId":"67cc9774b33f786311ed41a5","id":"67cddff1bee2f0736f7c2f84","isSelected":true,"name":"Abhishek"},{"amountBorrowedForExpense":80,"amountOwedForExpense":0,"groupExpenseId":"67cddff1bee2f0736f7c2f80","groupMemberId":"67cc9787b33f786311ed41ae","id":"67cddff1bee2f0736f7c2f86","isSelected":true,"name":"Suraj"}],"totalAmountPaid":200}]""".trimIndent()
        val expensesGson =
            Gson().fromJson(expensesGsonString, object : TypeToken<List<Expense>>() {})
        expenses.addAll(expensesGson)
        expenses.forEach { expense ->
            val paidBy = expense.paidBy ?: return@forEach
            println("==> ${expense.totalAmountPaid} | ${paidBy.amountOwedForExpense}")
            for (i in participants.indices) {
                val p = participants[i]
                if (p.id == paidBy.groupMemberId) {
                    if (!expense.isSquareOff) {
                        p.amountOwedFromGroup += paidBy.amountOwedForExpense
                    } else {
                        p.amountReturnedToOwner = paidBy.amountOwedForExpense
                    }
                } else {
                    var amountBorrowed = 0.0
                    val participant =
                        expense.remainingParticipants.find { it.groupMemberId == p.id }
                    participant?.let {
                        amountBorrowed = it.amountBorrowedForExpense
                        if (!expense.isSquareOff) {
                            p.amountBorrowedFromGroup += amountBorrowed
                        } else {
                            p.amountReceivedFromBorrower = amountBorrowed
                        }
                    }
                    p.pendingPaymentsMapping.add(
                        PendingPayments(
                            paidBy.groupMemberId,
                            paidBy.name,
                            amountBorrowed.roundToTwoDecimal()
                        )
                    )
                }

                println("---")
                println("==> ${participants[i].name} | ${participants[i].amountOwedFromGroup}")
                println("==> ${participants[i].name} | ${p.amountOwedFromGroup}")
                println("---")
                participants[i] = p
            }
        }
        println(">> ${Gson().toJson(participants)}")
    }
}