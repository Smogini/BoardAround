package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.ui.components.ScreenTemplate

class EditMyProfile(private val navController: NavController) {

    @Composable
    fun ShowEditMyProfile() {

        ScreenTemplate(
            title = "Modifica profilo",
            navController,
            showBottomBar = false,
        ) {

        }
    }
}