package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

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