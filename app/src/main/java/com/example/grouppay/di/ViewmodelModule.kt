package com.example.grouppay.di


import com.example.grouppay.ui.features.addExpense.viewmodel.AddExpenseViewModel
import com.example.grouppay.ui.features.addGroup.viewModel.AddGroupViewModel
import com.example.grouppay.ui.features.groups.viewmodel.ExpensesViewModel
import com.example.grouppay.ui.features.participantDetails.viewModel.ParticipantDetailsViewModel
import com.example.grouppay.ui.features.groups.viewmodel.GroupViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val viewModelModule = module {
    viewModel { GroupViewModel(get(), get()) }
    viewModel { AddGroupViewModel(get()) }
    viewModel { ParticipantDetailsViewModel(get()) }
    viewModel { AddExpenseViewModel(get(), get()) }
    viewModel { ExpensesViewModel(get()) }
}