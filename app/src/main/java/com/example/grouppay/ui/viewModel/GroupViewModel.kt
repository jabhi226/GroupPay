package com.example.grouppay.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouppay.domain.Group
import com.example.grouppay.ui.repo.GroupRepository
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    val groupList = repository.getGroupList()

    fun saveNewGroup(group: Group) {
        viewModelScope.launch {
            repository.saveNewGroup(group)
        }
    }

    fun getGroupInformation(_id: ObjectId) = repository.getGroupInformation(_id)

}