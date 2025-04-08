package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route

@Composable
fun ShowEditMyProfile(navController: NavController) {

    ScreenTemplate(
        title = "Modifica profilo",
        currentRoute = Route.EditMyProfile,
        navController
    ) {

    }
}