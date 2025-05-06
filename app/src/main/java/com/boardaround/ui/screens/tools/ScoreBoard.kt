package com.boardaround.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.screens.ScreenTemplate
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryBrown

data class Player(
    var name: MutableState<String>,
    var points: MutableIntState = mutableIntStateOf(0)
)

@Composable
fun ShowScoreBoard(navController: NavController) {
    var numPlayers by remember { mutableIntStateOf(2) }
    val players = remember {
        mutableStateListOf(*List(6) { Player(name = mutableStateOf("Player ${it + 1}")) }.toTypedArray())
    }
    var selectedPlayerIndex by remember { mutableIntStateOf(0) } // Indice del giocatore selezionato per rinominarlo
    var playerName by remember { mutableStateOf(TextFieldValue(players[0].name.value)) }

    ScreenTemplate(
        title = "Segna punti",
        navController = navController
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Numero di giocatori: $numPlayers", color = PrimaryBrown)
                Slider(
                    value = numPlayers.toFloat(),
                    onValueChange = { numPlayers = it.toInt() },
                    valueRange = 1f..6f,
                    steps = 5,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                CustomTextField(
                    label = "Rinomina giocatore",
                    value = playerName,
                    onValueChange = { playerName = it },
                    modifier = Modifier.fillMaxWidth(),
//                    readOnly = numPlayers < 0,
                    trailingIcon = {
                        if (playerName.text.isNotEmpty()) {
                            CustomButtonIcon(
                                title = "Clear",
                                icon = Icons.Filled.Clear,
                                iconColor = Errors,
                                onClick = { playerName = TextFieldValue("") }
                            )
                        }
                    },
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.wrapContentSize()
                ) {
                    items(numPlayers) { index ->
                        Button(onClick = {
                            selectedPlayerIndex = index
                            playerName =
                                TextFieldValue(players[index].name.value)
                        }) {
                            Text(players[index].name.value)
                        }
                    }
                }

                CustomButton(
                    onClick = {
                        if (playerName.text.isNotEmpty()) {
                            players[selectedPlayerIndex].name.value = playerName.text
                        }
                    },
                    text = "Rinomina ${players[selectedPlayerIndex].name.value}"
                )

                // Mostra la lista di giocatori e i punteggi
                Column {
                    players.take(numPlayers).forEachIndexed { _, player ->
                        val maxPoints = players.take(numPlayers).maxOf { it.points.intValue }
                        val minPoints = players.take(numPlayers).minOf { it.points.intValue }
                        val textColor = when (player.points.intValue) {
                            maxPoints -> Color.Green
                            minPoints -> Color.Red
                            else -> PrimaryBrown
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = player.name.value,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Punti: ${player.points.intValue}",
                                color = textColor
                            )
                            CustomButtonIcon(
                                title = "Aggiungi punti",
                                icon = Icons.Filled.Add,
                                iconColor = PrimaryBrown,
                                onClick = { player.points.intValue++ }
                            )
                            CustomButtonIcon(
                                title = "Rimuovi punti",
                                icon = Icons.Filled.Remove,
                                iconColor = PrimaryBrown,
                                onClick = { player.points.intValue-- }
                            )
                        }
                    }
                }
            }
        }
    }
}
