package com.boardaround.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.ScreenTemplate

class Homepage(private val navController: NavController): ComponentActivity() {

    @Composable
    fun ShowHomePageScreen() {
        ScreenTemplate(
            title = "Homepage",
            currentRoute = Route.Homepage.route,
            navController,
            showBottomBar = true,
        ) {

        }
    }
}