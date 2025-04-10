package com.boardaround.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boardaround.ui.screens.ShowEditMyProfile
import com.boardaround.ui.screens.ShowGameInfo
import com.boardaround.ui.screens.ShowHomePageScreen
import com.boardaround.ui.screens.ShowInviteScreen
import com.boardaround.ui.screens.ShowLoginScreen
import com.boardaround.ui.screens.ShowDiceScreen
import com.boardaround.ui.screens.ShowMyProfileScreen
import com.boardaround.ui.screens.ShowNewEventScreen
import com.boardaround.ui.screens.ShowNewPostScreen
import com.boardaround.ui.screens.ShowProfileScreen
import com.boardaround.ui.screens.ShowRegisterScreen
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isUserLoggedIn()) Route.Homepage else Route.Login,
        enterTransition = { fadeIn(tween(1000)) },
        exitTransition = { fadeOut(tween(1000)) }
    ) {
        composable<Route.Homepage> {
            ShowHomePageScreen(navController, userViewModel)
        }
        composable<Route.Login> {
            ShowLoginScreen(navController, authViewModel)
        }
        composable<Route.Register> {
            ShowRegisterScreen(navController, authViewModel)
        }
        composable<Route.Invite> {
            ShowInviteScreen(navController)
        }
        composable<Route.NewEvent> {
            ShowNewEventScreen(navController)
        }
        composable<Route.Profile> {
            ShowProfileScreen(navController)
        }
        composable<Route.MyProfile> {
            ShowMyProfileScreen(navController, authViewModel)
        }
        composable<Route.EditMyProfile> {
            ShowEditMyProfile(navController)
        }
        composable<Route.GameInfo> {
            ShowGameInfo(navController)
        }
        composable<Route.Dice> {
            ShowDiceScreen(navController)
        }
        composable<Route.NewPost> {
            ShowNewPostScreen(navController)
        }
    }
}
