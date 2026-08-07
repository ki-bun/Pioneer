package com.ki_bun.pioneer.util

import com.ki_bun.pioneer.data.Item
import com.opencsv.CSVReader
import java.io.StringReader

fun parseCSV(csvText: String): List<Item> {
    val items = mutableListOf<Item>()
    CSVReader(StringReader(csvText)).use { reader ->
        val allLines = reader.readAll()
        if (allLines.isEmpty()) return emptyList()

        // Skip header
        allLines.drop(1).forEach { cols ->
            try {
                val item = Item(
                    title = cols[0],
                    description = cols.getOrNull(1) ?: "",
                    progress = cols.getOrNull(2)?.toIntOrNull() ?: 0,
                    total = cols.getOrNull(3)?.toIntOrNull(),
                    tags = if (cols.getOrNull(4) != null) cols.getOrNull(4)!!.split(",") else emptyList(),
                    unit = cols.getOrNull(5) ?: "",
                    packageName = cols.getOrNull(6) ?: ""
                )
                items.add(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    return items
}