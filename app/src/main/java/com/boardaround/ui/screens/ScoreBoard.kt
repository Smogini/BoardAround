package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.boardaround.ui.theme.PrimaryText

data class Player(var name: String, var points: Int)

@Composable
fun ScoreBoardScreen() {
    var numPlayers by remember { mutableIntStateOf(2) }
    val players by remember { mutableStateOf(List(6) { Player("Player ${it + 1}", 0) }) }
    var playerName by remember { mutableStateOf(TextFieldValue("")) } // Per il campo di testo di rinomina
    var selectedPlayerIndex by remember { mutableIntStateOf(0) } // Indice del giocatore selezionato per rinominarlo

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
            players.take(numPlayers).forEachIndexed { _, player ->
                val maxPoints = players.take(numPlayers).maxOf { it.points }
                val minPoints = players.take(numPlayers).minOf { it.points }
                val textColor = when(player.points) {
                    maxPoints -> Color.Green
                    minPoints -> Color.Red
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
