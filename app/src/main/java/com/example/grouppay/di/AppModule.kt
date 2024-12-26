package com.example.grouppay.di

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.Participant
import com.example.grouppay.domain.repo.GroupRepository
import com.example.grouppay.data.repo.GroupRepositoryImpl
import com.example.grouppay.ui.features.addExpense.view.AddExpenseViewModel
import com.example.grouppay.ui.features.addGroup.viewModel.AddGroupViewModel
import com.example.grouppay.ui.features.addParticipant.viewModel.AddParticipantViewModel
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import org.koin.dsl.module
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.viewModel

val appModule = module {
    single {

        val config = RealmConfiguration.Builder(
            schema = setOf(Participant::class, Expense::class, Group::class)
        )
            .name("group_pay")
            .schemaVersion(1)
            .build()

        Realm.open(config)
    }

    single<GroupRepository> {
        GroupRepositoryImpl(get())
    }

    viewModel {
        GroupViewModel(get())
    }
    viewModel {
        AddGroupViewModel(get())
    }
    viewModel {
        AddParticipantViewModel(get())
    }
    viewModel {
        AddExpenseViewModel(get())
    }
}