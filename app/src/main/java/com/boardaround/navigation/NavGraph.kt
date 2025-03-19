package com.boardaround.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boardaround.ui.screens.Homepage
import com.boardaround.ui.screens.Invite
import com.boardaround.ui.screens.Login
import com.boardaround.ui.screens.Register
import com.boardaround.ui.screens.NewEvent
import com.boardaround.ui.screens.Profile
import com.boardaround.ui.screens.MyProfile


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Homepage.route) {
            Homepage(navController).ShowHomePageScreen()
        }
        composable(Route.Login.route) {
            Login(navController).ShowLoginScreen()
        }
        composable(Route.Register.route) {
            Register(navController).ShowRegisterScreen()
        }
        composable(Route.Invite.route) {
            Invite(navController).ShowInviteScreen()
        }
        composable(Route.NewEvent.route) {
            NewEvent(navController).ShowNewEventScreen()
        }
        composable(Route.Profile.route) {
            Profile(navController).ShowProfileScreen()
        }
        composable(Route.MyProfile.route) {
            MyProfile(navController).ShowMyProfileScreen()
        }
    }
}
