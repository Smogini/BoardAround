package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowMyProfileScreen(navController: NavController, authViewModel: AuthViewModel){
    ScreenTemplate(
        title = "Profilo di ${authViewModel.retrieveUsername()}",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true,
    ) {
        CustomButton(
            onClick = {
                authViewModel.logout()
                navController.navigate(Route.Login) {
                    launchSingleTop = true
                }
            },
            text = "Esci dal profilo"
        )
    }
}