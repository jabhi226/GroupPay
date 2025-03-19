package com.example.grouppay.di

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.ExpenseMember
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.GroupMember
import com.example.grouppay.data.repository.ExpenseRepositoryImpl
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.data.repository.GroupRepositoryImpl
import com.example.grouppay.data.repository.MemberRepositoryImpl
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.domain.repository.MembersRepository
import com.example.grouppay.ui.features.addExpense.viewmodel.AddExpenseViewModel
import com.example.grouppay.ui.features.addGroup.viewModel.AddGroupViewModel
import com.example.grouppay.ui.features.groups.viewmodel.ExpensesViewModel
import com.example.grouppay.ui.features.participantDetails.viewModel.ParticipantDetailsViewModel
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import com.example.grouppay.ui.features.groups.viewmodel.SquareOffViewModel
import org.koin.dsl.module
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.viewModel

val appModule = module {
    single {

        val config = RealmConfiguration.Builder(
            schema = setOf(
                GroupMember::class,
                Expense::class,
                Group::class,
                ExpenseMember::class
            )
        )
            .name("group_pay")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.open(config)
    }

    factory<GroupRepository> {
        GroupRepositoryImpl(get())
    }

    factory<ExpenseRepository> {
        ExpenseRepositoryImpl(get())
    }

    factory<MembersRepository> {
        MemberRepositoryImpl(get())
    }

    viewModel {
        GroupViewModel(get(), get())
    }
    viewModel {
        AddGroupViewModel(get())
    }
    viewModel {
        ParticipantDetailsViewModel(get())
    }
    viewModel {
        AddExpenseViewModel(get(), get())
    }
    viewModel {
        ExpensesViewModel(get())
    }
    viewModel {
        SquareOffViewModel(get())
    }
}