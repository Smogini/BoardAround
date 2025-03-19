package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.ScreenTemplate

class MyProfile(private val navController: NavController) {

    @Composable
    fun ShowMyProfileScreen(){

        ScreenTemplate(
            title = "Il mio profilo",
            currentRoute = Route.MyProfile.route,
            navController,
            showBottomBar = true,
        ) {

        }
    }
}