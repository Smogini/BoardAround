package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.viewmodel.UserViewModel

class MyProfile(private val navController: NavController) {

    @Composable
    fun ShowMyProfileScreen(userViewModel: UserViewModel){
        if (userViewModel.isUserLoggedIn()) {
            ScreenTemplate(
                title = "Profilo di ${userViewModel.retrieveUsername()}",
                navController = navController,
                showBottomBar = true,
            ) {
                CustomButton(
                    onClick = {
                        userViewModel.logout()
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                        }
                    },
                    text = "Esci dal profilo"
                )
            }
        } else {
            navController.navigate(Route.Login) {
                launchSingleTop = true
            }
        }
    }
}