package com.boardaround.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.ScreenTemplate

class Homepage(private val navController: NavController): ComponentActivity() {

    @Composable
    fun ShowHomePageScreen() {
        ScreenTemplate(
            title = "Homepage",
            bottomBar = { BottomBar(navController) }
        ) {

        }
    }
}