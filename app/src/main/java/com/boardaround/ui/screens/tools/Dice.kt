package com.boardaround.ui.screens.tools

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

    val sumOfDice = results.sum()

    ScreenTemplate(
        title = "Dice roll",
        navController = navController,
    ) {
        item {
            Text("Number of dice: $numDice", color = PrimaryBrown)
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
                    results = emptyList()
                },
                text = "Roll the dice"
            )

            if (isRolling) {
                LaunchedEffect(numDice) {
                    results = rollDiceWithAnimation(numDice)
                    isRolling = false
                }
                Text("Calculating...", color = PrimaryBrown)
            }

            if (results.isNotEmpty() && !isRolling) {
                Text("Result:", color = PrimaryBrown)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    results.forEach { result ->
                        val rotation = remember { Animatable(0f) }
                        LaunchedEffect(result) {
                            rotation.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = LinearEasing
                                )
                            )
                            rotation.snapTo(0f)
                        }
                        Image(
                            painter = painterResource(id = getDiceImage(result)),
                            contentDescription = "$result",
                            modifier = Modifier
                                .size(64.dp)
                                .rotate(rotation.value)
                        )
                    }
                }
                Text("Sum of the dice: $sumOfDice", color = PrimaryBrown)
            }
        }
    }
}

fun rollDice(numDice: Int): List<Int> {
    return List(numDice) { Random.nextInt(1, 7) }
}

fun getDiceImage(result: Int): Int {
    return when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}

suspend fun rollDiceWithAnimation(numDice: Int): List<Int> {
    delay(500)
    return rollDice(numDice)
}
