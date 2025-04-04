package com.boardaround.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boardaround.ui.screens.EditMyProfile
import com.boardaround.ui.screens.GameInfo
import com.boardaround.ui.screens.Homepage
import com.boardaround.ui.screens.Invite
import com.boardaround.ui.screens.Login
import com.boardaround.ui.screens.NewPost
import com.boardaround.ui.screens.Map
import com.boardaround.ui.screens.MyProfile
import com.boardaround.ui.screens.NewEvent
import com.boardaround.ui.screens.Profile
import com.boardaround.ui.screens.Register
import com.boardaround.viewmodel.UserViewModel

@Composable
fun NavGraph(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(
        navController = navController,
        startDestination = if (userViewModel.isUserLoggedIn()) Route.Homepage else Route.Login,
        enterTransition = { fadeIn(tween(500)) },
        exitTransition = { fadeOut(tween(500)) }
    ) {
        composable<Route.Homepage> {
            Homepage(navController, userViewModel).ShowHomePageScreen()
        }
        composable<Route.Login> {
            Login(navController, userViewModel).ShowLoginScreen()
        }
        composable<Route.Register> {
            Register(navController, userViewModel).ShowRegisterScreen()
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
            MyProfile(navController, userViewModel).ShowMyProfileScreen()
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
        composable<Route.NewPost> {
            NewPost(navController).ShowNewPostScreen()
        }
    }
}
