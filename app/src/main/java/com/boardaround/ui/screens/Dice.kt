package com.boardaround.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.theme.PrimaryText
import kotlin.random.Random
import com.boardaround.R
import kotlinx.coroutines.delay

@Composable
fun ShowDiceScreen(navController: NavController) {
    var numDice by remember { mutableStateOf(1) }
    var results by remember { mutableStateOf(emptyList<Int>()) }
    var isRolling by remember { mutableStateOf(false) } // Stato per l'animazione

    // Calcolare la somma dei risultati
    val sumOfDice = results.sum()

    ScreenTemplate(
        title = "Lancio Dadi",
        currentRoute = Route.Dice,
        navController = navController,
        showBottomBar = true,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Numero di dadi: $numDice", color = PrimaryText)
            Slider(
                value = numDice.toFloat(),
                onValueChange = { numDice = it.toInt() },
                valueRange = 1f..6f,
                steps = 5,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            CustomButton(
                onClick = {
                    isRolling = true
                    results = emptyList() // Reset dei risultati precedenti
                },
                text = "Lancia i dadi"
            )

            if (isRolling) {
                LaunchedEffect(numDice) {
                    results = rollDiceWithAnimation(numDice)
                    isRolling = false
                }
                Text("Lancio in corso...", color = PrimaryText)
            }

            if (results.isNotEmpty() && !isRolling) {
                Text("Risultati:", color = PrimaryText)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    results.forEachIndexed { index, result ->
                        val rotation = remember { Animatable(0f) }
                        LaunchedEffect(result) {
                            rotation.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = LinearEasing
                                )
                            )
                            rotation.snapTo(0f) // Reset della rotazione
                        }
                        Image(
                            painter = painterResource(id = getDiceImage(result)),
                            contentDescription = "Dado con valore $result",
                            modifier = Modifier
                                .size(64.dp)
                                .rotate(rotation.value)
                        )
                    }
                }
                // Mostra la somma dei dadi
                Text("Somma dei dadi: $sumOfDice", color = PrimaryText)
            }
        }
    }
}

fun rollDice(numDice: Int): List<Int> {
    return List(numDice) { Random.nextInt(1, 7) }
}

// Funzione per ottenere l'ID della risorsa immagine del dado
fun getDiceImage(result: Int): Int {
    return when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1 // Valore di default
    }
}

// Funzione per lanciare i dadi con animazione
suspend fun rollDiceWithAnimation(numDice: Int): List<Int> {
    delay(500) // Breve ritardo per simulare il lancio
    return rollDice(numDice)
}
