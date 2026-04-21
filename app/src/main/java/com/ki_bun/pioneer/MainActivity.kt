package com.ki_bun.pioneer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ki_bun.pioneer.ui.theme.PioneerTheme
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.component.ProgressCard
import com.ki_bun.pioneer.data.AppDatabase
import com.ki_bun.pioneer.data.ItemDao

class MainActivity : ComponentActivity() {

    private val itemDao: ItemDao by lazy {
        AppDatabase.getDatabase(applicationContext).itemDao()
    }
    private val progressViewModel: ProgressViewModel by viewModels {
        ProgressViewModelFactory(itemDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PioneerTheme {

                val progressList by progressViewModel.progressList.collectAsState()
                var selectedItem by remember { mutableStateOf<Item?>(null) }

                if (showEditDialog && selectedItem != null) {
                    selectedItem?.let { item ->
                        EditDialog(
                            progressList = item,
                            onUpdate = { newItem ->
                                progressViewModel.updateItem(newItem)
                                showEditDialog = false
                            }
                        )
                    }
                }

                if (showDialog) {
                    InputDialog(progressViewModel = progressViewModel)
                }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showDialog = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.sharp_add_24),
                                contentDescription = "Add"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(progressList) { item ->
                            ProgressCard(item,
                                onDelete = {
                                    progressViewModel.deleteItem(item)
                                },
                                onEdit = {
                                    selectedItem = item
                                    showEditDialog = true
                                },
                                progressViewModel = progressViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}