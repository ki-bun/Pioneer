package com.ki_bun.pioneer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    var progress: Int,
    val total: Int?
)

