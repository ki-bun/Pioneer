package com.ki_bun.pioneer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ki_bun.pioneer.ProgressViewModel
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.util.nullToString

@Composable
fun ProgressCard(
    progressList: Item,
    onDelete: (Item) -> Unit,
    onEdit: (Item) -> Unit,
    progressViewModel: ProgressViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var updatedProgress by remember { mutableIntStateOf(progressList.progress) }
    val newTotal = progressList.total
    val totalString = newTotal ?: nullToString(newTotal)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier
                .width(360.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(painterResource(id = R.drawable.more_vert_24px), contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEdit(progressList)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onDelete(progressList)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = progressList.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 50.dp),
                    style = if (updatedProgress == progressList.total) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = progressList.description,
                    modifier = Modifier.padding(end = 50.dp),
                    style = if (updatedProgress == progressList.total) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (progressList.total != null) {
                    val indicator = updatedProgress / newTotal.toFloat()
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = { indicator }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (updatedProgress > 0) {
                                updatedProgress--
                                progressViewModel.updateItem(progressList.copy(progress = updatedProgress))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        contentPadding = PaddingValues((0.dp))
                    ) {
                        Icon(
                            painterResource(id = R.drawable.sharp_subtract_24),
                            contentDescription = "Subtract",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(text = "${progressList.progress} / $totalString", modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    Button(
                        onClick = {
                            if (updatedProgress < (newTotal ?: Int.MAX_VALUE)) {
                                updatedProgress++
                                progressViewModel.updateItem(progressList.copy(progress = updatedProgress))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        contentPadding = PaddingValues((0.dp))
                    ) {
                        Icon(
                            painterResource(id = R.drawable.sharp_add_24),
                            contentDescription = "Subtract",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}