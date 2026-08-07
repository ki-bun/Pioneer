package com.ki_bun.pioneer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.ki_bun.pioneer.Status

@Entity(tableName="items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    var progress: Int,
    val total: Int?,
    @ColumnInfo(defaultValue = "") val tags: List<String> = emptyList(),
    val imagePath: String? = null,
    val unit: String,
    val status: Status = Status.IN_PROGRESS,
    val packageName: String
)

class Converters {
    @TypeConverter
    fun fromTags(tags: List<String>): String {
        return tags.joinToString(",")
    }

    @TypeConverter
    fun toTags(data: String): List<String> {
        return if (data.isBlank()) emptyList()
        else data.split(",")
    }

    @TypeConverter
    fun fromStatus(status: Status): String = status.name

    @TypeConverter
    fun toStatus(value: String): Status {
        return Status.entries.find { it.name == value.trim().uppercase() } ?: Status.IN_PROGRESS
    }
}