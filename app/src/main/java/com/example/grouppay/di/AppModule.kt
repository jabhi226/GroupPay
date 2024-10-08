package com.example.grouppay.di

import com.example.grouppay.domain.Contribution
import com.example.grouppay.domain.GroupInfo
import com.example.grouppay.domain.Splitter
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
            schema = setOf(Splitter::class, Contribution::class, GroupInfo::class)
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