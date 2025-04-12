package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import org.osmdroid.util.GeoPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.boardaround.data.entities.Event
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomMapField
import com.boardaround.ui.components.Customswitch
import com.boardaround.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowNewEventScreen(navController: NavController, userViewModel: UserViewModel) {
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
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) } // Stato per la posizione selezionata
    var selectedGame by remember { mutableStateOf("") } // Stato per il gioco selezionato

    // Lista simulata di giochi (sostituire con i giochi dell'utente)
    val gamesList = listOf("Monopoly", "Catan", "Risk", "Uno", "Jenga")

    // Stato per il popup (dialog) che mostra i giochi
    var isDialogOpen by remember { mutableStateOf(false) }

    val formattedDateTime = selectedDateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())) ?: "Seleziona data e ora"

    ScreenTemplate(
        title = "Crea nuovo evento",
        currentRoute = Route.NewEvent,
        navController,
    ) {
        LazyColumn {
            item {
                Text("Inserisci nome evento", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
                CustomTextField(label = "Inserisci nome evento", value = eventNameState.value, onValueChange = { eventNameState.value = it })

                Text("Inserisci descrizione", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
                CustomTextField(label = "Inserisci descrizione", value = descriptionState.value, onValueChange = { descriptionState.value = it })

                Text("Seleziona data e ora evento", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
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
                        DatePicker(state = datePickerState)
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

                // Sezione Indirizzo
                Text("Inserisci indirizzo evento", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
                CustomMapField(
                    label = "Inserisci indirizzo evento",
                    value = addressState.value,
                    onValueChange = { addressState.value = it },
                    onSuggestionClick = { suggestion ->
                        addressState.value = TextFieldValue(suggestion.displayName)
                        selectedLocation = GeoPoint(suggestion.lat.toDouble(), suggestion.lon.toDouble())
                    }
                )

                // Pulsante "A cosa si gioca?"
                Text("Seleziona gioco per l'evento", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
                CustomButton(onClick = { isDialogOpen = true }, text = if (selectedGame.isEmpty()) "A cosa si gioca?" else "Gioco selezionato: $selectedGame")

                // Dialog per la selezione del gioco
                if (isDialogOpen) {
                    AlertDialog(
                        onDismissRequest = { isDialogOpen = false },
                        title = { Text("Seleziona un gioco") },
                        text = {
                            Column {
                                gamesList.forEach { game ->
                                    TextButton(onClick = {
                                        selectedGame = game
                                        isDialogOpen = false // Chiude il dialogo dopo la selezione
                                    }) {
                                        Text(game)
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { isDialogOpen = false }) {
                                Text("Chiudi")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Evento privato", color = PrimaryText)
                    Customswitch(
                        checked = isPrivateEvent,
                        onCheckedChange = { isPrivateEvent = it }
                    )
                }

                CustomButton(
                    onClick = {
                        val newEvent = Event(
                            name = eventNameState.value.text,
                            description = descriptionState.value.text,
                            address = addressState.value.text,
                            dateTime = selectedDateTime.toString(),
                            isPrivate = isPrivateEvent
                        )
                        if (selectedLocation != null) {
                            val message = if (isPrivateEvent) "Evento privato creato con successo" else "Evento pubblico creato con successo"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            userViewModel.createNewEvent(newEvent)
                            navController.navigateSingleTop(Route.Homepage)
                        } else {
                            Toast.makeText(context, "Seleziona un indirizzo sulla mappa", Toast.LENGTH_SHORT).show()
                        }
                    },
                    text = "Crea evento"
                )

                CustomButton(onClick = {
                    Toast.makeText(context, "Evento annullato con successo", Toast.LENGTH_SHORT).show()
                    navController.navigateSingleTop(Route.Homepage)
                }, text = "Annulla")
            }
        }
    }
}
