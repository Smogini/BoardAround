package com.boardaround.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Score
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.screens.tools.SetupTriviaScreen
import com.boardaround.ui.screens.tools.ShowCoinFlip
import com.boardaround.ui.screens.tools.ShowDiceScreen
import com.boardaround.ui.screens.tools.ShowScoreBoard
import com.boardaround.ui.screens.tools.ShowTokenScreen
import com.boardaround.viewmodel.TriviaViewModel

sealed class UtilTools(
    val name: String,
    val icon: ImageVector
) {
    data object Dice: UtilTools("Lancia dadi", Icons.Filled.Casino)
    data object CoinFlip: UtilTools("Testa o croce", Icons.Filled.QuestionMark)
    data object Token: UtilTools("Token", Icons.Filled.CardGiftcard)
    data object Trivia: UtilTools("Quizzettone", Icons.Filled.Quiz)
    data object ScoreBoard: UtilTools("Segna punti", Icons.Filled.Score)

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
fun ToolsMenu(navController: NavController) {
    val isExpanded = remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300), label = "rotationAnimation"
    )

    Box {
        CustomButtonIcon(
            title = "Menu",
            icon = Icons.Filled.ArrowUpward,
            iconColor = MaterialTheme.colorScheme.primary,
            onClick = { isExpanded.value = !isExpanded.value },
            rotationAngle = rotationAngle
        )

        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier.padding(top = 2.dp)
        ) {
            UtilTools.allTools.forEach { utilTools ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = utilTools.icon,
                                contentDescription = utilTools.name,
                                modifier = Modifier.size(20.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(utilTools.name)
                        }
                    },
                    onClick = {
                        UtilTools.selectTool(utilTools)
                        navController.navigateSingleTop(Route.UtilTools)
                    }
                )
            }
        }
    }
}