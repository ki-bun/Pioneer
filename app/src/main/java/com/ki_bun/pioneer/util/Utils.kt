package com.ki_bun.pioneer.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var countWarning by mutableStateOf("")
var totalWarning by mutableStateOf("")

fun validateCount(input: String, total: Int?) {
    if (input.isNotEmpty()) {
        if (!(isNumeric(input))) {
            countWarning = "Positive integer only"
        }
        else if  (total == null) {
            countWarning = ""
        } else {
            total.let {
                countWarning = if (input.toInt() > it) {
                    "Must be less than total"
                } else {
                    ""
                }
            }
        }
    }
    else if (input.isEmpty()) {
        countWarning = "Field cannot be empty"
    } else {
        countWarning = ""
    }
}

fun validateTotal(input: Int?, count: String) {

    if (input == null) {
        totalWarning = "This will return \"?\""
    } else {
        if (input.toString().isEmpty()) {
            totalWarning = ""
        } else if (count.isNotEmpty() && count.toIntOrNull() != null) {
            input.let {
                totalWarning = if (it < count.toInt()) {
                    "Must be empty or greater than progress"
                } else ""
            }
        } else if (count.toIntOrNull() == null) {
            totalWarning = ""
        }
        }
}

// If the value is null, it will show a question mark for the progress card to display
fun nullToString(value: Int?): String {
    return value?.toString() ?: "?"
}

/* Checks if a number is numeric, this doesn't accept negative and decimal numbers and
 other non-numeric characters,and will start from 0 onwards
 */
fun isNumeric(str: String): Boolean {
    return str.matches(Regex("^(0|[1-9]\\d*)$"))
}

fun normalizeTags(tags: String): List<String> {
    return tags.split(",")
        .map { it.trim().lowercase() }
        .filter { it.isNotEmpty() }
        .distinct()
}