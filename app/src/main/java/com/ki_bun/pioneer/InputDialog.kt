package com.ki_bun.pioneer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ki_bun.pioneer.data.Item
import com.ki_bun.pioneer.util.countWarning
import com.ki_bun.pioneer.util.isNumeric
import com.ki_bun.pioneer.util.totalWarning
import com.ki_bun.pioneer.util.validateCount
import com.ki_bun.pioneer.util.validateTotal
import kotlinx.coroutines.flow.filter

var showDialog by mutableStateOf(false)

@Composable
fun InputDialog(progressViewModel: ProgressViewModel) {

    val focusRequester = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current

    var inputTitle by remember {mutableStateOf("")}
    var inputCount by remember {mutableStateOf("")}
    var inputDescription by remember {mutableStateOf("")}
    var inputTotal: String? by remember {mutableStateOf("")}
    var newTotal: Int? by remember {mutableStateOf(null)}

    val maxTitle = 70
    val maxDescription = 100
    val maxCount = 4

    countWarning = "Field cannot be empty"

    fun resetValues() {
        inputTitle = ""
        inputCount = ""
        inputTotal = ""
        inputDescription = ""
        totalWarning = ""
        newTotal = null
    }

    Dialog(
        onDismissRequest = {
            showDialog = false
            resetValues()
        }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add Progress",
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Title:", fontSize = 12.sp)
                OutlinedTextField(
                    value = inputTitle,
                    onValueChange = { if (it.length in 0..maxTitle) inputTitle = it },
                    placeholder = { Text(text = "Enter title") },
                    singleLine = true,
                    supportingText = {
                        if (inputTitle.isEmpty()) {
                            Text(
                                text = "Field cannot be empty",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    isError = inputTitle.isEmpty(),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                )
                Text(text = "Description (Optional):", fontSize = 12.sp)
                OutlinedTextField(
                    value = inputDescription,
                    singleLine = true,
                    onValueChange = { inputDescription =
                        if (it.length <= maxDescription && it.isNotEmpty()) it else "" },
                    supportingText = {
                        Text(
                            text = "${inputDescription.length} / $maxDescription",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    placeholder = { Text(text = "Enter a brief description") }
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
                                Text(text = countWarning,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        ) }
                    Text(text = "/")
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
                                Text(text = totalWarning,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {showDialog = false}) {
                        Text("Dismiss")
                    }
                    TextButton(
                        enabled = !(inputTitle.isEmpty()) &&
                                isNumeric(inputCount) &&
                                !(inputCount.isEmpty()) &&
                                (newTotal?.let { it >= inputCount.toInt() } ?: true),
                        onClick = {
                            val newItem = Item(
                                title = inputTitle,
                                description = inputDescription,
                                progress = inputCount.toInt(),
                                total = newTotal
                            )
                            progressViewModel.addItem(newItem)

                            showDialog = false
                            resetValues()
                        }) {
                        Text("Confirm")
                    }
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
