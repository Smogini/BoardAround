package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.ui.components.ScreenTemplate

class GameInfo(private val navController: NavController) {

    @Composable
    fun ShowGameInfo(){

        ScreenTemplate(
            title = "Scheda del gioco :",
            navController,
            showBottomBar = false,
        ) {

        }
    }
}