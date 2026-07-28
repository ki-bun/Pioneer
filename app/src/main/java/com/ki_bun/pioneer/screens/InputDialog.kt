package com.ki_bun.pioneer.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.util.countWarning
import com.ki_bun.pioneer.util.isNumeric
import com.ki_bun.pioneer.util.normalizeTags
import com.ki_bun.pioneer.util.totalWarning
import com.ki_bun.pioneer.util.validateCount
import com.ki_bun.pioneer.util.validateTotal
import kotlinx.coroutines.flow.filter
import java.io.File
import java.io.FileOutputStream

var showDialog by mutableStateOf(false)

// Character limits
const val maxTitle = 70
const val maxDescription = 100
const val maxCount = 4

// InputDialog's behavior changes depending on the state of this variable
var isEditing by mutableStateOf(false)

@Composable
fun InputDialog(
    progressList: Item?,
    onUpdate: (Item) -> Unit,
    onDismiss: () -> Unit
    ) {

    val focusRequester = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current
    val context = LocalContext.current

    // Use the current values if you are editing
    var inputTitle by remember { mutableStateOf(if (isEditing) progressList!!.title else "") }
    var inputCount by remember { mutableStateOf(if (isEditing) progressList!!.progress.toString() else "") }
    var inputDescription by remember { mutableStateOf(if (isEditing) progressList!!.description else "") }
    var inputTotal: String? by remember {
        mutableStateOf(
            if (isEditing) progressList!!.total?.toString() ?: "" else ""
        )
    }
    var inputTags by remember { mutableStateOf(if (isEditing) progressList!!.tags.joinToString(",") else "") }
    var inputImage by remember { mutableStateOf(if (isEditing) progressList!!.imagePath else null) }
    var inputUnit by remember {mutableStateOf(if (isEditing) progressList!!.unit else "")}

    var newTotal: Int? by remember { mutableStateOf(null) }

    countWarning = if (isEditing) "" else "Field cannot be empty"

    fun resetValues() {
        inputTitle = ""
        inputCount = ""
        inputTotal = ""
        inputDescription = ""
        totalWarning = ""
        newTotal = null
    }

    fun saveImageToInternalStorage(
        context: Context,
        uri: Uri
    ): String {
        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val savedPath = saveImageToInternalStorage(context, uri)

            inputImage = savedPath
        }
    }
    Dialog(
        onDismissRequest = {
            showDialog = false
            resetValues()
            if (isEditing) {
                onDismiss()
            } else {
                showDialog = false
                resetValues()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (isEditing) {
                                onDismiss()
                            } else {
                                showDialog = false
                                resetValues()
                            }
                        }) {
                            Icon(painterResource(id = R.drawable.close_24px), contentDescription = "Close")
                        }
                        Text(
                            text = if (isEditing) "Edit Progress" else "Add Progress",
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        enabled = !(inputTitle.isEmpty()) &&
                                isNumeric(inputCount) &&
                                !(inputCount.isEmpty()) &&
                                (newTotal?.let { it >= inputCount.toInt() } ?: true),
                        onClick = {
                            if (isEditing) {
                                val newItem = progressList!!.copy(
                                    title = inputTitle,
                                    description = inputDescription,
                                    progress = inputCount.toInt(),
                                    total = if (inputTotal != progressList.total.toString()) newTotal else progressList.total,
                                    tags = normalizeTags(inputTags),
                                    imagePath = inputImage,
                                    unit = inputUnit
                                )
                                onUpdate(newItem)
                                onDismiss()
                            } else {
                                val newItem = Item(
                                    title = inputTitle,
                                    description = inputDescription,
                                    progress = inputCount.toInt(),
                                    total = newTotal,
                                    tags = normalizeTags(inputTags),
                                    imagePath = inputImage,
                                    unit = inputUnit
                                )
                                onUpdate(newItem)
                                resetValues()
                            }
                        }) {
                        Icon(painterResource(
                            id = R.drawable.send_24px),
                            contentDescription = "Enter"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .width(360.dp)
                            .height(150.dp)
                            .clickable(
                                onClick = {
                                    launcher.launch("image/*")
                                }
                            )
                            .background(MaterialTheme.colorScheme.secondaryContainer)

                    ) {
                        if (inputImage != null) {
                            AsyncImage(
                                model = File(inputImage!!),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.matchParentSize(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    IconButton(
                                        onClick = {
                                            inputImage = null
                                        }
                                    ) {
                                        Icon(painterResource(id = R.drawable.delete_24px),
                                            contentDescription = "Delete image",
                                            tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.hide_image_24px),
                                    contentDescription = "No image"
                                )
                                Text(text = "Click to select an image (Optional)", fontSize = 14.sp)
                            }
                        }
                    }
                Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Title:", fontSize = 12.sp)
                        OutlinedTextField(
                            value = inputTitle,
                            onValueChange = { if (it.length in 0..maxTitle) inputTitle = it },
                            placeholder = { Text(text = "Enter title") },
                            singleLine = true,
                            supportingText = {
                                Text(
                                    text = if (inputTitle.isEmpty()) {
                                        "Field cannot be empty"
                                    } else "${inputTitle.length} / $maxTitle",
                                    color = if (inputTitle.isEmpty()) {
                                        MaterialTheme.colorScheme.error
                                    } else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            isError = inputTitle.isEmpty(),
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {
                                Text(text = "Current progress:", fontSize = 12.sp)
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    placeholder = { Text(text = "Current") },
                                    value = inputCount,
                                    onValueChange = { newText ->
                                        if (newText.length <= maxCount) {
                                            inputCount = newText
                                        }
                                        validateCount(inputCount, newTotal)
                                    },
                                    isError = !(isNumeric(inputCount)) && inputCount.isEmpty(),
                                    supportingText = {
                                        Text(
                                            text = countWarning,
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            Text(
                                text = "/",
                                Modifier.offset(y = 30.dp),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Thin
                            )
                            Column {
                                Text(text = "Total (Optional):", fontSize = 12.sp)
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    placeholder = { Text(text = "Total") },
                                    value = inputTotal.orEmpty(),
                                    onValueChange = { newText ->
                                        if (newText.length <= maxCount) {
                                            inputTotal = newText
                                        }
                                        newTotal = when {
                                            newText.isEmpty() -> null
                                            else -> newText.toIntOrNull()
                                        }
                                        validateTotal(newTotal, inputCount)
                                    },
                                    supportingText = {
                                        Text(
                                            text = totalWarning,
                                            color = if (newTotal == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }
                Text(text = "Description (Optional):", fontSize = 12.sp)
                OutlinedTextField(
                    value = inputDescription,
                    onValueChange = { if (it.length in 0..maxDescription) inputDescription = it },
                    supportingText = {
                        Text(
                            text = "${inputDescription.length} / $maxDescription",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    placeholder = { Text(text = "Enter a brief description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "Tags (Optional):", fontSize = 12.sp)
                OutlinedTextField(
                    value = inputTags,
                    onValueChange = { inputTags = it },
                    singleLine = true,
                    placeholder = { Text(text = "Enter tags separated by commas") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "Unit (Optional):", fontSize = 12.sp)
                        OutlinedTextField(
                            modifier = Modifier.width(100.dp),
                            placeholder = { Text(text = "ep, ch, ...") },
                            value = inputUnit,
                            onValueChange = { newText ->
                                if (newText.length <= 5) {
                                    inputUnit = newText
                                }
                            }
                        )
                    }
                }
            }
        }
        LaunchedEffect(windowInfo) {
            snapshotFlow { windowInfo.isWindowFocused }
                .filter { it }
                .collect {
                    focusRequester.requestFocus()
                }
        }
    }
}