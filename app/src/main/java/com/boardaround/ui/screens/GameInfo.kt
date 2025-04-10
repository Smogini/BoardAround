package com.boardaround.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route

@Composable
fun ShowGameInfo(navController: NavController) {
    // Stato per il popup (dialog) che mostra la descrizione del gioco
    var isDialogOpen by remember { mutableStateOf(false) }

    // Funzione per chiudere il dialog
    val closeDialog: () -> Unit = { isDialogOpen = false }

    // Stato per "Aggiungi ai miei giochi"
    var isGameAdded by remember { mutableStateOf(false) }

    // Funzione per aggiungere il gioco
    val addGameToFavorites = {
        isGameAdded = true
        // Qui puoi aggiungere la logica per effettivamente aggiungere il gioco
        // ad esempio, memorizzarlo nelle preferenze o in un database.
        println("Gioco aggiunto ai miei giochi!")
    }

    // Contenuto principale della schermata
    ScreenTemplate(
        title = "Scheda del gioco :",
        currentRoute = Route.GameInfo,
        navController = navController,
        showBottomBar = true
    ) { contentPadding ->

        // Layout principale
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Titolo del gioco
            Text(
                text = "Nome del Gioco", // Nome del gioco da visualizzare
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            // Breve descrizione del gioco
            Text(
                text = "Gioco divertente e coinvolgente per tutti!",
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
                onClick = { addGameToFavorites() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Aggiungi ai miei giochi")
            }

            // Stato del gioco aggiunto
            if (isGameAdded) {
                Text(
                    text = "Gioco aggiunto ai tuoi giochi!",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
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

