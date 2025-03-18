package com.example.boardaround.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.boardaround.screens.HomepageScreen
import com.example.boardaround.screens.LoginScreen
import com.example.boardaround.screens.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Homepage.route) {
            HomepageScreen(navController)
        }
        composable(Route.Login.route) {
            LoginScreen()
        }
        composable(Route.Register.route) {
            RegisterScreen()
        }

    }
}
