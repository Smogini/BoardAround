package com.boardaround.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boardaround.ui.screens.HomepageScreen
import com.boardaround.ui.screens.LoginScreen
import com.boardaround.ui.screens.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Homepage.route) {
            HomepageScreen(navController)
        }
        composable(Route.Login.route) {
            LoginScreen(navController)
        }
        composable(Route.Register.route) {
            RegisterScreen()
        }

    }
}
