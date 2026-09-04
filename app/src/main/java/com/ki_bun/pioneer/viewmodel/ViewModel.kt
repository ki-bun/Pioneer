package com.ki_bun.pioneer.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.data.ItemDao
import com.ki_bun.pioneer.util.CSVExport
import com.ki_bun.pioneer.util.parseCSV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(private val itemDao: ItemDao) : ViewModel() {

    private val _progressList = MutableStateFlow<List<Item>>(emptyList())
    val progressList: StateFlow<List<Item>> = _progressList

    // Load all items on app start
    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            itemDao.getAllItems().collect {
                items ->
                _progressList.value = items
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
            loadItems()
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
            loadItems()
        }
    }

    fun updateList(items: List<Item>) {
        val updatedItems = items.mapIndexed { index, item ->
            item.copy(position = index)
        }

        _progressList.value = updatedItems

        viewModelScope.launch {
            itemDao.updateAll(updatedItems)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
            loadItems()
        }
    }

    fun exportToUri(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {

            val items = itemDao.getAllItemsOnce()
            val csv = CSVExport.itemsToCSV(items)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(csv.toByteArray())
            }
        }
    }

    fun importFromCSV(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val csvText = context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
                csvText?.let {
                    val items = parseCSV(it)
                    // Insert all items into Room
                    itemDao.insertAll(items)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

class ProgressViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgressViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass")
    }
}