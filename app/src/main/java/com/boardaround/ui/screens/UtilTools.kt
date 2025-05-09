package com.boardaround.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.screens.tools.SetupTriviaScreen
import com.boardaround.ui.screens.tools.ShowCoinFlip
import com.boardaround.ui.screens.tools.ShowDiceScreen
import com.boardaround.ui.screens.tools.ShowScoreBoard
import com.boardaround.ui.screens.tools.ShowTokenScreen
import com.boardaround.viewmodel.TriviaViewModel

sealed class UtilTools(
    val name: String
) {
    data object Dice: UtilTools("Lancia dadi")
    data object CoinFlip: UtilTools("Testa o croce")
    data object Token: UtilTools("Token")
    data object Trivia: UtilTools("Quizzettone")
    data object ScoreBoard: UtilTools("Segna punti")

    companion object {
        val allTools = listOf(Dice, CoinFlip, Token, Trivia, ScoreBoard)
        private var selectedTool: UtilTools = Dice

        fun selectTool(selected: UtilTools) {
            selectedTool = selected
        }

        fun getSelectedTool(): UtilTools = selectedTool

    }
}

@Composable
fun ShowToolScreen(navController: NavController, triviaViewModel: TriviaViewModel) {
    val selectedTool = UtilTools.getSelectedTool()

    when (selectedTool) {
        is UtilTools.Dice -> ShowDiceScreen(navController)
        is UtilTools.CoinFlip -> ShowCoinFlip(navController)
        is UtilTools.Token -> ShowTokenScreen(navController)
        is UtilTools.Trivia -> SetupTriviaScreen(navController, triviaViewModel)
        is UtilTools.ScoreBoard -> ShowScoreBoard(navController)
    }
}

@Composable
fun ToolsMenu(navController: NavController, isExpanded: MutableState<Boolean>) {

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300), label = "rotationAnimation"
    )

    Box {
        IconButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowUpward,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.rotate(rotationAngle)
            )
        }

        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = !isExpanded.value },
            modifier = Modifier.padding(top = 2.dp)
        ) {
            UtilTools.allTools.forEach { utilTools ->
                DropdownMenuItem(
                    text = { Text(utilTools.name) },
                    onClick = {
                        UtilTools.selectTool(utilTools)
                        navController.navigateSingleTop(Route.UtilTools)
                    }
                )
            }
        }
    }
}