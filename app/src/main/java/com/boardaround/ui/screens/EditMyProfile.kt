package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

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