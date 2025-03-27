package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton

class MyProfile(private val navController: NavController) {

    @Composable
    fun ShowMyProfileScreen(){

        ScreenTemplate(
            title = "Il mio profilo",
            navController = navController,
            showBottomBar = true,
        ) {

            CustomButton(
                onClick = {
                    navController.navigate(Route.EditMyProfile) {
                        launchSingleTop = true
                    }
                },
                text = "Modifica il mio profilo"
            )

        }
    }
}