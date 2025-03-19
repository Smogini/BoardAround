package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.ScreenTemplate

class EditMyProfile(private val navController: NavController) {

    @Composable
    fun ShowEditMyProfile() {

        ScreenTemplate(
            title = "Modifica profilo",
            currentRoute = Route.EditMyProfile.route,
            navController,
            showBottomBar = false,
        ) {

        }
    }
}