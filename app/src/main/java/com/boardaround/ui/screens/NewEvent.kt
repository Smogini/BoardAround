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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.boardaround.data.getCurrentUser
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.Customswitch
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime

@Composable
fun ShowNewEventScreen(navController: NavController, eventViewModel: EventViewModel, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    val eventNameState = remember { mutableStateOf(TextFieldValue()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }
    val addressState = remember { mutableStateOf(TextFieldValue()) }
    var isPrivateEvent by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedGame by remember { mutableStateOf("") }

    var selectedDateTime by remember { mutableStateOf("Seleziona data e ora") }
    var showDateTimePicker by remember { mutableStateOf(false) }

    val username = context.getCurrentUser().username
    val userGames by gameViewModel.userGames.collectAsState(initial = emptyList())

    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        gameViewModel.getUserGames(username)
    }

    ScreenTemplate(
        title = "Crea nuovo evento",
        currentRoute = Route.NewEvent,
        navController,
    ) {
        LazyColumn {
            item {
                Text("Inserisci nome evento", textAlign = TextAlign.Center, color = PrimaryBrown, modifier = Modifier.fillMaxWidth())
                CustomTextField(label = "Inserisci nome evento", value = eventNameState.value, onValueChange = { eventNameState.value = it })

                Text("Inserisci descrizione", textAlign = TextAlign.Center, color = PrimaryBrown, modifier = Modifier.fillMaxWidth())
                CustomTextField(label = "Inserisci descrizione", value = descriptionState.value, onValueChange = { descriptionState.value = it })

                Text("Seleziona data e ora evento", textAlign = TextAlign.Center, color = PrimaryBrown, modifier = Modifier.fillMaxWidth())
                CustomButton(onClick = { showDateTimePicker = true }, text = selectedDateTime)

                if (showDateTimePicker) {
                    DateTimePicker(
                        initialDateTime = LocalDateTime.now(),
                        onDateTimeSelected = { _, formattedDateTime ->
                            selectedDateTime = formattedDateTime
                            showDateTimePicker = false
                        },
                        onDismiss = { showDateTimePicker = false }
                    )
                }

                Text("Inserisci indirizzo evento", textAlign = TextAlign.Center, color = PrimaryBrown, modifier = Modifier.fillMaxWidth())
//                CustomMapField(
//                    label = "Inserisci indirizzo evento",
//                    value = addressState.value,
//                    onValueChange = { addressState.value = it },
//                    onSuggestionClick = { suggestion ->
//                        addressState.value = TextFieldValue(suggestion.displayName)
//                        selectedLocation = GeoPoint(suggestion.lat.toDouble(), suggestion.lon.toDouble())
//                    }
//                )
                CustomTextField(
                    label = "Inserisci indirizzo",
                    value = addressState.value,
                    onValueChange = { addressState.value = it }
                )

                Text("Seleziona gioco per l'evento", textAlign = TextAlign.Center, color = PrimaryBrown, modifier = Modifier.fillMaxWidth())
                CustomButton(onClick = { isDialogOpen = true }, text = if (selectedGame.isEmpty()) "A cosa si gioca?" else "Gioco selezionato: $selectedGame")

                if (isDialogOpen) {
                    AlertDialog(
                        onDismissRequest = { isDialogOpen = false },
                        title = { Text("Seleziona un gioco") },
                        text = {
                            Column {
                                userGames.forEach { game -> // Usa userGames invece di gamesList
                                    TextButton(onClick = {
                                        selectedGame = game.name
                                        isDialogOpen = false
                                    }) {
                                        Text(game.name)
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
                    Text("Evento privato", color = PrimaryBrown)
                    Customswitch(
                        checked = isPrivateEvent,
                        onCheckedChange = { isPrivateEvent = it }
                    )
                }

                CustomButton(
                    onClick = {
                        val newEvent = Event(
                            name = eventNameState.value.text,
                            author = username,
                            description = descriptionState.value.text,
                            address = addressState.value.text,
                            dateTime = selectedDateTime,
                            isPrivate = isPrivateEvent,
                        )
//                        if (selectedLocation != null) {
                            val message = if (isPrivateEvent) "Evento privato creato con successo" else "Evento pubblico creato con successo"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            eventViewModel.insertEvent(newEvent)
                            navController.navigateSingleTop(Route.Homepage)
//                        } else {
//                            Toast.makeText(context, "Seleziona un indirizzo sulla mappa", Toast.LENGTH_SHORT).show()
//                        }
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
