package com.example.grouppay.ui.features.addGroup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.data.repo.GroupRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class AddGroupViewModel (
    private val repository: GroupRepository
): ViewModel() {

    val saveResponse: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun saveNewGroup(groupName: String) {
        viewModelScope.launch {
            repository.saveNewGroup(groupName)
            saveResponse.emit(true)
        }
    }
}