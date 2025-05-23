package com.boardaround.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boardaround.ui.screens.GamificationScreen
import com.boardaround.ui.screens.ShowEditMyProfile
import com.boardaround.ui.screens.ShowEventInfoScreen
import com.boardaround.ui.screens.ShowGameInfo
import com.boardaround.ui.screens.ShowHomePageScreen
import com.boardaround.ui.screens.ShowInviteScreen
import com.boardaround.ui.screens.ShowLoginScreen
import com.boardaround.ui.screens.ShowMyProfileScreen
import com.boardaround.ui.screens.ShowNewEventScreen
import com.boardaround.ui.screens.ShowNewPostScreen
import com.boardaround.ui.screens.ShowProfileScreen
import com.boardaround.ui.screens.ShowRegisterScreen
import com.boardaround.ui.screens.ShowNotificationScreen
import com.boardaround.ui.screens.ShowToolScreen
import com.boardaround.ui.screens.SplashScreen
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.TriviaViewModel
import com.boardaround.viewmodel.UserViewModel
import com.boardaround.viewmodel.NewsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    gameViewModel: GameViewModel,
    postViewModel: PostViewModel,
    eventViewModel: EventViewModel,
    triviaViewModel: TriviaViewModel,
    newsViewModel: NewsViewModel,
    onThemeChange: (Boolean) -> Unit
) {
    val startDest = if(authViewModel.isUserLoggedIn()) Route.Homepage else Route.Splash
    NavHost(
        navController = navController,
        startDestination = startDest,
        enterTransition = { fadeIn(tween(500)) },
        exitTransition = { fadeOut(tween(500)) }
    ) {
        composable<Route.Splash> {
            SplashScreen(navController)
        }
        composable<Route.Homepage> {
            ShowHomePageScreen(navController, userViewModel, gameViewModel, eventViewModel, newsViewModel)
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
            ShowNewEventScreen(navController, eventViewModel, gameViewModel, userViewModel)
        }
        composable<Route.Profile> {
            ShowProfileScreen(navController, userViewModel)
        }
        composable<Route.MyProfile> {
            ShowMyProfileScreen(navController, authViewModel, postViewModel, userViewModel, eventViewModel, gameViewModel)
        }
        composable<Route.EditMyProfile> {
            ShowEditMyProfile(navController, onThemeChange = onThemeChange)
        }
        composable<Route.GameInfo> {
            ShowGameInfo(navController, gameViewModel)
        }
        composable<Route.NewPost> {
            ShowNewPostScreen(navController, postViewModel, userViewModel)
        }
        composable<Route.EventInfo> {
            ShowEventInfoScreen(navController, userViewModel, eventViewModel)
        }
        composable<Route.Gamification> {
            GamificationScreen(navController, userViewModel)
        }
        composable<Route.UtilTools> {
            ShowToolScreen(navController, triviaViewModel)
        }
        composable<Route.NotificationCenter> {
            ShowNotificationScreen(navController)
        }
    }
}

fun NavController.navigateSingleTop(route: Route) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateSingleTop.graph.startDestinationId) {
            saveState = true
        }
    }
}