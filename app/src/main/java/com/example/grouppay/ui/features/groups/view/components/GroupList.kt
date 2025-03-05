package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grouppay.ui.Testing.getGroupWithTotalExpenses
import com.example.grouppay.domain.entities.GroupWithTotalExpense

@Preview(showSystemUi = true)
@Composable
fun GroupList(
    modifier: Modifier = Modifier,
    groups: List<GroupWithTotalExpense> = getGroupWithTotalExpenses(),
    navigateToGroup: (GroupWithTotalExpense) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp, 4.dp)
    ) {
        items(groups) {
            GroupItem(group = it) { navigateToGroup(it) }
        }
    }
}
