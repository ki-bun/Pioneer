package com.ki_bun.pioneer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.data.Item

@Composable
fun TagsScreen(tags: List<Item>) {
    val tagCount = tags.flatMap { it.tags }.groupingBy { it }.eachCount()
    val totalTagCount = tags.flatMap { it.tags }.distinct().size

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(painterResource(id = R.drawable.tag_24px), contentDescription = "Tag Icon")
            Text(
                text = "All Tags",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "$totalTagCount",
                    modifier = Modifier.padding(7.dp))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                FlowRow(
                    modifier = Modifier.padding(15.dp)
                ) {
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
    }
}
