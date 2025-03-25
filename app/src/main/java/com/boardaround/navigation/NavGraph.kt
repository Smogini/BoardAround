package com.boardaround.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boardaround.ui.screens.Homepage
import com.boardaround.ui.screens.Invite
import com.boardaround.ui.screens.Login
import com.boardaround.ui.screens.Register
import com.boardaround.ui.screens.NewEvent
import com.boardaround.ui.screens.Profile
import com.boardaround.ui.screens.MyProfile
import com.boardaround.ui.screens.EditMyProfile
import com.boardaround.ui.screens.GameInfo
import com.boardaround.ui.screens.Map
import com.boardaround.viewmodel.UserViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = Route.Login) {
        composable<Route.Homepage> {
            Homepage(navController).ShowHomePageScreen(userViewModel)
        }
        composable<Route.Login> {
            Login(navController).ShowLoginScreen(userViewModel)
        }
        composable<Route.Register> {
            Register(navController).ShowRegisterScreen(userViewModel)
        }
        composable<Route.Invite> {
            Invite(navController).ShowInviteScreen()
        }
        composable<Route.NewEvent> {
            NewEvent(navController).ShowNewEventScreen()
        }
        composable<Route.Profile> {
            Profile(navController).ShowProfileScreen()
        }
        composable<Route.MyProfile> {
            MyProfile(navController).ShowMyProfileScreen()
        }
        composable<Route.EditMyProfile> {
            EditMyProfile(navController).ShowEditMyProfile()
        }
        composable<Route.GameInfo> {
            GameInfo(navController).ShowGameInfo()
        }
        composable<Route.Map> {
            Map(navController).ShowMapScreen()
        }
    }
}
