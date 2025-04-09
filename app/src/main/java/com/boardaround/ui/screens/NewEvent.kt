package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.Customswitch
import com.boardaround.ui.theme.PrimaryText
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowNewEventScreen(navController: NavController) {
    val eventNameState = remember { mutableStateOf(TextFieldValue()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    val addressState = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    var isPrivateEvent by remember { mutableStateOf(false) }

    val formattedDateTime = selectedDateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())) ?: "Seleziona data e ora"


    ScreenTemplate(
        title = "Crea nuovo evento",
        currentRoute = Route.NewEvent,
        navController,
    ) {
        Column {
            Text("Inserisci nome evento", textAlign = TextAlign.Center, color = PrimaryText, modifier=Modifier.fillMaxWidth())
            CustomTextField(label = "Inserisci nome evento", value = eventNameState.value, onValueChange = { eventNameState.value = it })

            Text("Inserisci descrizione", textAlign = TextAlign.Center, color = PrimaryText, modifier=Modifier.fillMaxWidth())
            CustomTextField(label = "Inserisci descrizione", value = descriptionState.value, onValueChange = { descriptionState.value = it })

            Text("Seleziona data e ora evento", textAlign = TextAlign.Center, color = PrimaryText, modifier=Modifier.fillMaxWidth())
            CustomButton(onClick = { showDatePicker = true }, text = formattedDateTime)

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        CustomButton(onClick = {
                            showDatePicker = false
                            showTimePicker = true
                        }, text = "Avanti")
                    },
                    dismissButton = {
                        CustomButton(onClick = { showDatePicker = false }, text = "Annulla")
                    }
                ) {
                    androidx.compose.material3.DatePicker(state = datePickerState)
                }
            }

            if (showTimePicker) {
                AlertDialog(
                    onDismissRequest = { showTimePicker = false },
                    title = { Text("Seleziona ora") },
                    text = { TimePicker(state = timePickerState) },
                    confirmButton = {
                        CustomButton(onClick = {
                            showTimePicker = false
                            val selectedDateMillis = datePickerState.selectedDateMillis ?: 0L
                            val selectedDate = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                            selectedDateTime = LocalDateTime.of(selectedDate.year, selectedDate.month, selectedDate.dayOfMonth, timePickerState.hour, timePickerState.minute)
                        }, text = "OK")
                    },
                    dismissButton = {
                        CustomButton(onClick = { showTimePicker = false }, text = "Annulla")
                    }
                )
            }



            Text("Inserisci indirizzo evento", textAlign = TextAlign.Center, color = PrimaryText, modifier=Modifier.fillMaxWidth())
            CustomTextField(
                label = "Inserisci indirizzo evento",
                value = addressState.value,
                onValueChange = { addressState.value = it }
            )

            Spacer(modifier = Modifier.height(150.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Evento privato", color = PrimaryText)
                Customswitch()
            }


                CustomButton(
                    onClick = {
                        val message = if (isPrivateEvent) "Evento privato creato con successo" else "Evento pubblico creato con successo"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        navController.navigate(Route.Homepage) {
                            launchSingleTop = true
                        }
                    },
                    text = "Crea evento"
                )


            CustomButton(onClick = {
                Toast.makeText(context, "Evento annullato con successo", Toast.LENGTH_SHORT).show()
                navController.navigate(Route.Homepage) {
                    launchSingleTop = true
                }
            }, text = "Annulla")
        }
    }
}