package com.ki_bun.pioneer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.Status
import com.ki_bun.pioneer.component.ProgressCard
import com.ki_bun.pioneer.data.Item
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.flatMap

@Composable
fun HomeScreen(
    progressViewModel: ProgressViewModel,
    progress: List<Item>,
    navController: NavController
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var selectedTag by remember { mutableStateOf<String?>(null) }
    val ongoingItems = progress.filter {
        (selectedTag == null || selectedTag!! in it.tags) && it.status == Status.IN_PROGRESS
    }
    val completedItems = progress.filter {
        (selectedTag == null || selectedTag!! in it.tags) && it.status == Status.COMPLETED
    }
    val archivedItems = progress.filter {
        (selectedTag == null || selectedTag!! in it.tags) && it.status == Status.ARCHIVED
    }
    var showFilter by remember { mutableStateOf(false) }
    var showCompleted by remember { mutableStateOf(false) }
    var showArchived by remember { mutableStateOf(false) }

    if (showDialog) {
        InputDialog(
            progressList = null,
            onUpdate = { newItem ->
                progressViewModel.addItem(newItem)
                showDialog = false
            },
            onDismiss = {}
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.size(80.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sharp_add_24),
                    contentDescription = "Add",
                    Modifier.size(40.dp)
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("settings")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_24px),
                        contentDescription = "Settings"
                    )
                }
                IconButton(
                    onClick = {
                        showFilter = !showFilter
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.filter_alt_24px),
                        contentDescription = "Show filter by tags"
                    )
                }
            }
            if (showFilter) {
                LazyRow {
                    item {
                        progress.flatMap { it.tags }.groupingBy { it }.eachCount().forEach { (tag,_) ->
                            FilterChip(
                                selected = selectedTag == tag,
                                onClick = {
                                    selectedTag = if (selectedTag == tag) null else tag
                                },
                                label = { Text(text = tag) },
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }
                }
            }
            LazyColumn {
                items(ongoingItems) { item ->
                    ProgressCard(
                        item,
                        onDelete = {
                            progressViewModel.deleteItem(item)
                        },
                        onEdit = {
                            selectedItem = item
                            isEditing = true
                        },
                        progressViewModel = progressViewModel
                    )
                }
                item {
                    Button(
                        onClick = { showCompleted = !showCompleted },
                        modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)
                    ) {
                        Text(text = if (showCompleted) "Hide Completed" else "Show Completed")
                    }
                }
                if (showCompleted) {
                    items(completedItems) { item ->
                        ProgressCard(
                            item,
                            onDelete = {
                                progressViewModel.deleteItem(item)
                            },
                            onEdit = {
                                selectedItem = item
                                isEditing = true
                            },
                            progressViewModel = progressViewModel
                        )
                    }
                }
                item {
                    Button(
                        onClick = { showArchived = !showArchived },
                        modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)
                    ) {
                        Text(text = if (showArchived) "Hide Archived" else "Show Archived")
                    }
                }
                if (showArchived) {
                    items(archivedItems) { item ->
                        ProgressCard(
                            item,
                            onDelete = {
                                progressViewModel.deleteItem(item)
                            },
                            onEdit = {
                                selectedItem = item
                                isEditing = true
                            },
                            progressViewModel = progressViewModel
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

        }
        if (isEditing && selectedItem != null) {
            selectedItem?.let { item ->
                InputDialog(
                    progressList = item,
                    onUpdate = { newItem ->
                        progressViewModel.updateItem(newItem)
                        isEditing = false
                    },
                    onDismiss = {
                        isEditing = false
                    }
                )
            }
        }
    }
}