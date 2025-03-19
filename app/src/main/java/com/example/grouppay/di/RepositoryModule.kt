package com.example.grouppay.di

import com.example.grouppay.data.repository.ExpenseRepositoryImpl
import com.example.grouppay.domain.repository.GroupRepository
import com.example.grouppay.data.repository.GroupRepositoryImpl
import com.example.grouppay.data.repository.MemberRepositoryImpl
import com.example.grouppay.domain.repository.ExpenseRepository
import com.example.grouppay.domain.repository.MembersRepository
import org.koin.dsl.module


val repositoryModule = module {
    factory<GroupRepository> { GroupRepositoryImpl(get()) }
    factory<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
    factory<MembersRepository> { MemberRepositoryImpl(get()) }
}