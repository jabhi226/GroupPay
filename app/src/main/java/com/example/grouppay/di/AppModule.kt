package com.example.grouppay.di

import com.example.grouppay.domain.Expense
import com.example.grouppay.domain.Group
import com.example.grouppay.domain.Participant
import com.example.grouppay.ui.repo.GroupRepository
import com.example.grouppay.ui.repo.impl.GroupRepositoryImpl
import com.example.grouppay.ui.viewModel.GroupViewModel
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
}