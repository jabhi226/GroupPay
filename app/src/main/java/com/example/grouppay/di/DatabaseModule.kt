package com.example.grouppay.di

import com.example.grouppay.data.entities.Expense
import com.example.grouppay.data.entities.ExpenseMember
import com.example.grouppay.data.entities.Group
import com.example.grouppay.data.entities.GroupMember
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val databaseModule = module {
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
}