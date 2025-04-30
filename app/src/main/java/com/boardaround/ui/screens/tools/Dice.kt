package com.boardaround.ui.screens.tools

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.R
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.screens.ScreenTemplate
import com.boardaround.ui.theme.PrimaryBrown
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ShowDiceScreen(navController: NavController) {
    var numDice by remember { mutableIntStateOf(1) }
    var results by remember { mutableStateOf(emptyList<Int>()) }
    var isRolling by remember { mutableStateOf(false) }

    // Calcolare la somma dei risultati
    val sumOfDice = results.sum()

    ScreenTemplate(
        title = "Lancio Dadi",
        navController = navController,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Numero di dadi: $numDice", color = PrimaryBrown)
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
                Text("Lancio in corso...", color = PrimaryBrown)
            }

            if (results.isNotEmpty() && !isRolling) {
                Text("Risultati:", color = PrimaryBrown)
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
                Text("Somma dei dadi: $sumOfDice", color = PrimaryBrown)
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
