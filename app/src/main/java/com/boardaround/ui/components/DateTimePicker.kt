package com.boardaround.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    initialDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime, String) -> Unit,
    onDismiss: () -> Unit,
    showTimePicker: Boolean = true
) {
    val showDatePickerState = remember { mutableStateOf(true) }
    val showTimePickerState = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(is24Hour = true)

    var selectedDateTime by remember { mutableStateOf(initialDateTime) }
    var formattedDateTime: String

    if (showDatePickerState.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerState.value = false },
            confirmButton = {
                CustomButton(
                    onClick = {
                        showDatePickerState.value = false
                        if(!showTimePicker) {
                            selectedDateTime = selectDateTime(datePickerState, timePickerState)
                            formattedDateTime = formatDateTime(selectedDateTime, false)
                            onDateTimeSelected(selectedDateTime, formattedDateTime)
                        } else {
                            showTimePickerState.value = true
                        }
                    },
                    text = if (showTimePicker) "Avanti" else "OK"
                )
            },
            dismissButton = {
                CustomButton(onClick = onDismiss, text = "Annulla")
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePickerState.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text("Seleziona ora") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                CustomButton(
                    onClick = {
                        selectedDateTime = selectDateTime(datePickerState, timePickerState)
                        formattedDateTime = formatDateTime(selectedDateTime, true)
                        onDateTimeSelected(selectedDateTime, formattedDateTime)
                    },
                    text = "OK"
                )
            },
            dismissButton = {
                CustomButton(onClick = onDismiss, text = "Annulla")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun selectDateTime(datePickerState: DatePickerState, timePickerState: TimePickerState): LocalDateTime {
    val selectedDateMillis = datePickerState.selectedDateMillis ?: 0L
    val selectedDate = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
    val selectedDateTime = LocalDateTime.of(
        selectedDate.year,
        selectedDate.month,
        selectedDate.dayOfMonth,
        timePickerState.hour,
        timePickerState.minute
    )

    return selectedDateTime
}

private fun formatDateTime(dateTime: LocalDateTime, withTime: Boolean): String {
    val pattern = if(withTime) "dd/MM/yyyy HH:mm" else "dd/MM/yyyy"
    return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}
