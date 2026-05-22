package com.ki_bun.pioneer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ki_bun.pioneer.data.Item

@Composable
fun TagsScreen(tags: List<Item>) {
    val tagCount = tags.flatMap { it.tags }.groupingBy { it }.eachCount()

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "All Tags",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 20.dp)
            )
        FlowRow {
            tagCount.forEach { (tag, count) ->
                AssistChip(
                    onClick = {},
                    label = { Text(
                        text = "#$tag     $count"
                    ) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }
    }
}
