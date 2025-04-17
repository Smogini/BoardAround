package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowGameInfo(navController: NavController, gameViewModel: GameViewModel, userViewModel: UserViewModel) {
    val gameToShow by gameViewModel.selectedGame.collectAsState()
    val username = userViewModel.getCurrentUser()!!.username
    val context = LocalContext.current

    // Stato per il popup (dialog) che mostra la descrizione del gioco
    var isDialogOpen by remember { mutableStateOf(false) }
    val closeDialog: () -> Unit = { isDialogOpen = false }

    // Ottieni la lista dei giochi dell'utente
    val userGames by gameViewModel.getUserGames(username).collectAsState(initial = emptyList())

    // Controlla se il gioco è già nella lista dell'utente
    val isGameAdded = remember(userGames, gameToShow) {
        userGames.contains(gameToShow?.nameElement?.value.toString())
    }

    ScreenTemplate(
        title = "Scheda del gioco",
        currentRoute = Route.GameInfo,
        navController = navController
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = gameToShow?.nameElement?.value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = gameToShow?.description.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Numero di giocatori (minimo e massimo)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Giocatori: Minimo 2, Massimo 6", style = MaterialTheme.typography.bodyMedium)
            }

            // Pulsante "Come si gioca?" che apre il dialog con la descrizione
            Button(
                onClick = { isDialogOpen = true },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Come si gioca?")
            }

            // Pulsante "Aggiungi ai miei giochi"
            Button(
                onClick = {
                    if (gameToShow != null) {
                        val gameName = gameToShow!!.nameElement.value
                        if (isGameAdded) {
                            gameViewModel.removeGame(username, gameName)
                            Toast.makeText(context, "Gioco rimosso dai tuoi giochi", Toast.LENGTH_SHORT).show()
                        } else {
                            gameViewModel.addGame(username, gameName)
                            Toast.makeText(context, "Gioco aggiunto ai tuoi giochi", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(if (isGameAdded) "Rimuovi dai miei giochi" else "Aggiungi ai miei giochi")
            }

            // Dialog che mostra la descrizione del gioco quando il tasto viene premuto
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = closeDialog,
                    title = { Text("Come si gioca") },
                    text = {
                        Text("Questo gioco è un'avventura epica dove i giocatori devono cooperare per superare ostacoli e risolvere enigmi!")
                    },
                    confirmButton = {
                        TextButton(onClick = closeDialog) {
                            Text("Chiudi")
                        }
                    }
                )
            }
        }
    }
}
