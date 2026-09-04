package com.ki_bun.pioneer.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.Status
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.util.nullToString
import java.io.File

@Composable
fun ProgressCard(
    progressList: Item,
    onEdit: (Item) -> Unit,
    progressViewModel: ProgressViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var updatedProgress = progressList.progress
    val newTotal = progressList.total
    val totalString = newTotal ?: nullToString(newTotal)
    val uriHandler = LocalUriHandler.current
    var newStatus: Status
    var isArchived = progressList.status == Status.ARCHIVED
    val context = LocalContext.current
    var itemToDelete by remember { mutableStateOf<Item?>(null)}

    if (itemToDelete != null) {
        Dialog(
            onDismissRequest = {
                itemToDelete = null
            }
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text("Confirm delete?")
                    Row {
                        TextButton(
                            onClick = {itemToDelete = null}
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                progressViewModel.deleteItem(itemToDelete!!)
                                itemToDelete = null
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
        ) {

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                if (progressList.imagePath != null) {
                    AsyncImage(
                        model = File(progressList.imagePath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize(),
                        alpha = 0.3f
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                painterResource(id = R.drawable.more_vert_24px),
                                contentDescription = "More options"
                            )
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
                                    itemToDelete = progressList
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (!isArchived) "Archive" else "Unarchive") },
                                onClick = {
                                    if (!isArchived) {
                                        newStatus = Status.ARCHIVED
                                        progressViewModel.updateItem(progressList.copy(status = newStatus))
                                        isArchived = true
                                    } else {
                                        newStatus = Status.IN_PROGRESS
                                        progressViewModel.updateItem(progressList.copy(status = newStatus))
                                        isArchived = false
                                        expanded = false
                                    }
                                }
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    FlowRow {
                        progressList.tags.forEach { tag ->
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(end = 3.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = R.drawable.tag_24px),
                                        contentDescription = "Tag icon",
                                        modifier = Modifier.size(12.dp))
                                    Text(tag, fontSize = 10.sp, modifier = Modifier.padding(start = 5.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = progressList.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 50.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Annotate website urls enclosed in <>
                    val annotatedString = buildAnnotatedString {
                        val str = progressList.description
                        val regex =
                            Regex("[<＜].*?\\..*?[>＞]") // Allow either unicode or ascii characters of < and >
                        val https = Regex("^https://")
                        var lastIndex = 0
                        if (regex.containsMatchIn(str)) {
                            regex.findAll(str).forEach { link ->
                                val url = link.value.drop(1).dropLast(1)
                                append(str.substring(lastIndex, link.range.first))
                                withLink(
                                    LinkAnnotation.Clickable(
                                    tag = "URL",
                                    linkInteractionListener = {
                                        if (https.containsMatchIn(url)) {
                                            uriHandler.openUri(url)
                                        } else {
                                            uriHandler.openUri("https:$url")
                                        }
                                    }
                                )) {
                                    append(url)
                                }
                                lastIndex = link.range.last + 1
                            }
                            append(str.substring(lastIndex))
                        } else {
                            append(str)
                        }
                    }
                    Text(
                        annotatedString,
                        modifier = Modifier.padding(end = 50.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (progressList.total != null) {
                        val indicator = progressList.progress / newTotal.toFloat()
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${(indicator * 100).toInt()} %", fontSize = 14.sp)
                            LinearProgressIndicator(
                                modifier = Modifier.padding(start = 5.dp).fillMaxWidth(),
                                progress = { indicator },
                                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (progressList.packageName.isNotEmpty()) {
                            IconButton(onClick = {
                                val intent = context.packageManager.getLaunchIntentForPackage(progressList.packageName)
                                Log.d("Launch","Intent = $intent")
                                context.startActivity(intent)

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_exit_to_app_24),
                                    contentDescription = "app"
                                )
                            }
                        }
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
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp),
                                contentPadding = PaddingValues((0.dp))
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.sharp_subtract_24),
                                    contentDescription = "Subtract"
                                )
                            }
                            Text(
                                text = "${progressList.unit} ${progressList.progress} / $totalString",
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Button(
                                onClick = {
                                    if (updatedProgress < (newTotal ?: Int.MAX_VALUE)) {
                                        updatedProgress++
                                        progressViewModel.updateItem(progressList.copy(progress = updatedProgress))
                                    }
                                },
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp),
                                contentPadding = PaddingValues((0.dp))
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.sharp_add_24),
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }
                }
            }
        }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (!isArchived) {
                newStatus = if (updatedProgress == progressList.total) Status.COMPLETED else Status.IN_PROGRESS
                progressViewModel.updateItem(progressList.copy(status = newStatus))
            }
        }