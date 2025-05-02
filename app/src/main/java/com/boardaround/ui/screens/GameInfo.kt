package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boardaround.data.entities.toSavedGame
import com.boardaround.data.getCurrentUser
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.ExpandableSection
import com.boardaround.viewmodel.GameViewModel

@Composable
fun ShowGameInfo(navController: NavController, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    val username = context.getCurrentUser().username
    val gameToShow by gameViewModel.selectedGame.collectAsState()

    var showGameDescription by remember { mutableStateOf(false) }
    var showExpansions by remember { mutableStateOf(false) }
    val closeDialog: () -> Unit = { showGameDescription = false }

    val userGames by gameViewModel.userGames.collectAsState(initial = emptyList())

    val isGameAdded = remember(userGames, gameToShow) {
        userGames.contains(gameToShow?.toSavedGame(username))
    }

    gameViewModel.getUserGames(username)

    ScreenTemplate(
        title = "Scheda del gioco",
        currentRoute = Route.GameInfo,
        navController = navController
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gameToShow?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = "Nome del gioco: " + gameToShow?.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Editore: " + gameToShow?.publisher,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Numero giocatori",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Minimo: ${gameToShow?.minPlayers}, Massimo: ${gameToShow?.maxPlayers}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Tempo di gioco: ${gameToShow?.playingTime} minuti",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                ExpandableSection(
                    title = "Espansioni",
                    items = gameToShow?.expansions ?: emptyList(),
                    isExpanded = showExpansions,
                    onExpandChange = { showExpansions = !showExpansions },
                    onItemClick = { game ->
                        gameViewModel.getGameInfo(game.id)
                        navController.navigateSingleTop(Route.GameInfo)
                    }
                ) { game ->
                    Text(game.title)
                }

                Button(
                    onClick = { showGameDescription = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Come si gioca?")
                }

                // Pulsante "Aggiungi ai miei giochi"
                Button(
                    onClick = {
                        if (gameToShow != null) {
                            val gameToSave = gameToShow!!.toSavedGame(username)
                            if (isGameAdded) {
                                gameViewModel.removeSavedGame(gameToSave)
                                Toast.makeText(context, "Gioco rimosso dai tuoi giochi", Toast.LENGTH_SHORT).show()
                            } else {
                                gameViewModel.addGame(gameToSave)
                                Toast.makeText(context, "Gioco aggiunto ai tuoi giochi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(if (isGameAdded) "Rimuovi dai miei giochi" else "Aggiungi ai miei giochi")
                }

                // Dialog che mostra la descrizione del gioco quando il tasto viene premuto
                if (showGameDescription) {
                    AlertDialog(
                        onDismissRequest = closeDialog,
                        title = { Text("Come si gioca") },
                        text = {
                            LazyColumn {
                                item {
                                    Text("${gameToShow?.description}")
                                }
                            }
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
}
