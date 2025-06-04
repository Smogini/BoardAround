package com.boardaround.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.screens.ScreenTemplate
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
    var selectedPlayerIndex by remember { mutableIntStateOf(0) }
    var playerName by remember { mutableStateOf(TextFieldValue(players[0].name.value)) }

    ScreenTemplate(
        title = "Scoreboard",
        navController = navController
    ) {
        item {
            Text("Number of players: $numPlayers", color = PrimaryBrown)
            Slider(
                value = numPlayers.toFloat(),
                onValueChange = { numPlayers = it.toInt() },
                valueRange = 1f..6f,
                steps = 5,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            CustomTextField(
                label = "Rename player",
                value = playerName.text,
                onValueChange = { playerName = TextFieldValue(it) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (playerName.text.isNotEmpty()) {
                        CustomClickableIcon(
                            title = "Clear",
                            icon = Icons.Default.Clear,
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
                text = "Rename ${players[selectedPlayerIndex].name.value}"
            )

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
                            text = "Points: ${player.points.intValue}",
                            color = textColor
                        )
                        CustomClickableIcon(
                            title = "Add point",
                            icon = Icons.Default.Add,
//                            iconColor = PrimaryBrown,
                            onClick = { player.points.intValue++ }
                        )
                        CustomClickableIcon(
                            title = "Remove point",
                            icon = Icons.Default.Remove,
//                            iconColor = PrimaryBrown,
                            onClick = { player.points.intValue-- }
                        )
                    }
                }
            }
        }
    }
}
