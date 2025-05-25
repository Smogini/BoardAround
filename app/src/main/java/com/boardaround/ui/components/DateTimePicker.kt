package com.boardaround.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    isVisible: Boolean,
    showTimePicker: Boolean = true,
    onResult: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val showDatePicker = remember { mutableStateOf(true) }
    val showTimePickerState = remember { mutableStateOf(showTimePicker) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(is24Hour = true)

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker.value = false
                    if (showTimePicker) {
                        showTimePickerState.value = true
                    } else {
                        val formatted = formatSelectedDateTime(
                            datePickerState.selectedDateMillis,
                            null
                        )
                        onResult(formatted)
                        onDismiss()
                    }
                }) {
                    Text(if (showTimePicker) "Next" else "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePickerState.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text("Select time") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(onClick = {
                    val formatted = formatSelectedDateTime(
                        datePickerState.selectedDateMillis,
                        timePickerState
                    )
                    onResult(formatted)
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun formatSelectedDateTime(
    dateMillis: Long?,
    timeState: TimePickerState?
): String {
    val formatter = if (timeState != null) {
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    } else {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    val localDate = Instant.ofEpochMilli(dateMillis ?: 0L)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return if (timeState != null) {
        val dateTime = LocalDateTime.of(
            localDate.year,
            localDate.month,
            localDate.dayOfMonth,
            timeState.hour,
            timeState.minute
        )
        dateTime.format(formatter)
    } else {
        localDate.format(formatter)
    }
}
