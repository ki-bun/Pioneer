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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.component.ProgressCard
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.ui.theme.ThemeMode
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.flatMap

@Composable
fun HomeScreen(
    progressViewModel: ProgressViewModel,
    tags: List<Item>,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var selectedTag by remember { mutableStateOf<String?>(null) }
    val filteredItems = tags.filter {
        selectedTag == null || selectedTag!! in it.tags
    }
    val navController = rememberNavController()
    var showFilter by remember { mutableStateOf(false) }

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
                        tags.flatMap { it.tags }.groupingBy { it }.eachCount().forEach { (tag,_) ->
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
                items(filteredItems) { item ->
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

    // Navigation
    NavHost(
        navController = navController,
        startDestination = "homescreen",
    ) {
        composable("homescreen") {}
        composable("settings") {
            SettingsScreen(
                onThemeModeChange = onThemeModeChange,
                themeMode = themeMode,
                progressViewModel = progressViewModel,
                navController
            )
        }
    }

}