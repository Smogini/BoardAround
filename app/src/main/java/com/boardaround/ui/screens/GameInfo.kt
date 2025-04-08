package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route

@Composable
fun ShowGameInfo(navController: NavController){

    ScreenTemplate(
        title = "Scheda del gioco :",
        currentRoute = Route.GameInfo,
        navController = navController,
        showBottomBar = true
    ) {

    }
}