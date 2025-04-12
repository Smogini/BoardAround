package com.boardaround.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import com.boardaround.ui.theme.PrimaryText
import kotlin.random.Random

data class Player(var name: String, var points: Int)

@Composable
fun ScoreBoardScreen() {
    var numPlayers by remember { mutableStateOf(2) }  // Numero di giocatori (da 1 a 6)
    var players by remember { mutableStateOf(List(6) { Player("Player ${it + 1}", 0) }) } // Lista di giocatori
    var playerName by remember { mutableStateOf(TextFieldValue("")) } // Per il campo di testo di rinomina
    var selectedPlayerIndex by remember { mutableStateOf(0) } // Indice del giocatore selezionato per rinominarlo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selezione del numero di giocatori
        Text("Numero di giocatori: $numPlayers", color = PrimaryText)
        Slider(
            value = numPlayers.toFloat(),
            onValueChange = { numPlayers = it.toInt() },
            valueRange = 1f..6f,
            steps = 5,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        // Input per rinominare il giocatore selezionato
        TextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Rinomina Giocatore") },
            modifier = Modifier.fillMaxWidth(),
            enabled = numPlayers > 0
        )

        // Seleziona il giocatore da rinominare
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (0 until numPlayers).forEach { index ->
                Button(onClick = {
                    selectedPlayerIndex = index
                    playerName = TextFieldValue(players[index].name)  // Precompone il nome del giocatore selezionato
                }) {
                    Text(players[index].name)
                }
            }
        }

        // Button per rinominare il giocatore selezionato
        Button(
            onClick = {
                players[selectedPlayerIndex].name = playerName.text
            },
            enabled = playerName.text.isNotEmpty()
        ) {
            Text("Rinomina Giocatore ${selectedPlayerIndex + 1}")
        }

        // Mostra la lista di giocatori e i punteggi
        Column {
            players.take(numPlayers).forEachIndexed { index, player ->
                val maxPoints = players.take(numPlayers).maxOf { it.points }
                val minPoints = players.take(numPlayers).minOf { it.points }
                val textColor = when {
                    player.points == maxPoints -> Color.Green
                    player.points == minPoints -> Color.Red
                    else -> PrimaryText
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = player.name,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Punti: ${player.points}",
                        color = textColor
                    )
                    IconButton(onClick = { player.points++ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Aggiungi Punti")
                    }
                    IconButton(onClick = { if (player.points > 0) player.points-- }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Rimuovi Punti")
                    }
                }
            }
        }
    }
}
