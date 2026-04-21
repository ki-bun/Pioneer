package com.ki_bun.pioneer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.data.ItemDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(private val itemDao: ItemDao) : ViewModel() {

    private val _progressList = MutableStateFlow<List<Item>>(emptyList())
    val progressList: StateFlow<List<Item>> = _progressList

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

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
            loadItems()
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