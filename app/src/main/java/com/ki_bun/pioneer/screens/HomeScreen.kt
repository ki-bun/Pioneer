package com.ki_bun.pioneer.screens

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.Status
import com.ki_bun.pioneer.component.ProgressCard
import com.ki_bun.pioneer.data.Item
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.flatMap

@Composable
fun HomeScreen(
    progressViewModel: ProgressViewModel,
    progress: List<Item>,
    navController: NavController,
    onListChange: (List<Item>) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
    var selectedList by remember { mutableStateOf(Status.IN_PROGRESS)}
    val lazyListState = rememberLazyListState()
    val displayedItems = when (selectedList) {
        Status.COMPLETED -> completedItems
        Status.ARCHIVED -> archivedItems
        else -> ongoingItems
    }
    val reorderableLazyListState =
        rememberReorderableLazyListState(lazyListState) { from, to ->

            val newList = progress.toMutableList()

            val fromItem = displayedItems[from.index]
            val toItem = displayedItems[to.index]

            val fromIndex = newList.indexOfFirst { it.id == fromItem.id }
            val toIndex = newList.indexOfFirst { it.id == toItem.id }

            val item = newList.removeAt(fromIndex)
            newList.add(toIndex, item)

            onListChange(newList)
        }

    if (showDialog) {
        InputDialog(
            progressList = null,
            onUpdate = { newItem ->
                progressViewModel.addItem(newItem)
                showDialog = false
            },
            onDismiss = {},
            allItems = progress
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text(text = "In Progress") },
                    selected = selectedList == Status.IN_PROGRESS,
                    onClick = {
                        selectedList = Status.IN_PROGRESS
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Completed") },
                    selected = selectedList == Status.COMPLETED,
                    onClick = {
                        selectedList = Status.COMPLETED
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Archived") },
                    selected = selectedList == Status.ARCHIVED,
                    onClick = {
                        selectedList = Status.ARCHIVED
                    }
                )
            }
        }
    ) {
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
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.baseline_menu_24), contentDescription = "menu")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                }
                if (showFilter) {
                    LazyRow {
                        item {
                            if (progress.flatMap{it.tags}.isEmpty()) {
                                Text("No tags to show")
                            } else {
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
                }
                LazyColumn(state = lazyListState) {
                    items(displayedItems, key = { it.id }
                    ) { item ->
                        ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                            Surface(
                                modifier = Modifier.longPressDraggableHandle(),
                                shadowElevation = elevation
                            ) {
                                ProgressCard(
                                    item,
                                    onEdit = {
                                        selectedItem = item
                                        isEditing = true
                                    },
                                    progressViewModel = progressViewModel
                                )
                            }
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
                        },
                        allItems = progress
                    )
                }
            }
        }
    }
}