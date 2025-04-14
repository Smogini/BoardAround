package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.boardaround.data.entities.Event
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomMapField
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.Customswitch
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.UserViewModel
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime

@Composable
fun ShowNewEventScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val eventNameState = remember { mutableStateOf(TextFieldValue()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }
    val addressState = remember { mutableStateOf(TextFieldValue()) }
    var isPrivateEvent = false
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) } // Stato per la posizione selezionata
    var selectedGame by remember { mutableStateOf("") } // Stato per il gioco selezionato

    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var showDateTimePicker by remember { mutableStateOf(false) }
    var formattedDateTime by remember { mutableStateOf("Seleziona data e ora") }

    val gamesList = listOf("Monopoly", "Catan", "Risk", "Uno", "Jenga")

    var isDialogOpen by remember { mutableStateOf(false) }

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
                CustomButton(onClick = { showDateTimePicker = true }, text = formattedDateTime)

                if (showDateTimePicker) {
                    DateTimePicker(
                        initialDateTime = selectedDateTime,
                        onDateTimeSelected = { dateTime, format ->
                            selectedDateTime = dateTime
                            formattedDateTime = format
                            showDateTimePicker = false
                        },
                        onDismiss = { showDateTimePicker = false }
                    )
                }

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

                Text("Seleziona gioco per l'evento", textAlign = TextAlign.Center, color = PrimaryText, modifier = Modifier.fillMaxWidth())
                CustomButton(onClick = { isDialogOpen = true }, text = if (selectedGame.isEmpty()) "A cosa si gioca?" else "Gioco selezionato: $selectedGame")

                if (isDialogOpen) {
                    AlertDialog(
                        onDismissRequest = { isDialogOpen = false },
                        title = { Text("Seleziona un gioco") },
                        text = {
                            Column {
                                gamesList.forEach { game ->
                                    TextButton(onClick = {
                                        selectedGame = game
                                        isDialogOpen = false
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
