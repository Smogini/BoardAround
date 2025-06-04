package com.boardaround.navigation

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
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
import com.boardaround.ui.screens.ShowNotificationScreen
import com.boardaround.ui.screens.ShowProfileScreen
import com.boardaround.ui.screens.ShowRegisterScreen
import com.boardaround.ui.screens.ShowToolScreen
import com.boardaround.ui.screens.SplashScreen
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.NotificationViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.TriviaViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun NavGraph(
    context: Context,
    navController: NavHostController,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    gameViewModel: GameViewModel,
    postViewModel: PostViewModel,
    eventViewModel: EventViewModel,
    triviaViewModel: TriviaViewModel,
    notificationViewModel: NotificationViewModel,
    onThemeChange: (Boolean) -> Unit
) {
    val startDest = if(authViewModel.isUserLoggedIn()) Route.Homepage else Route.Splash
    NavHost(
        navController = navController,
        startDestination = startDest,
        enterTransition = {
            scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500))
        },
        popEnterTransition = {
            scaleIn(
                initialScale = 1.1f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500))
        }
    ) {
        composable<Route.Splash> {
            SplashScreen(navController)
        }
        composable<Route.Homepage> {
            ShowHomePageScreen(navController, userViewModel, gameViewModel, eventViewModel)
        }
        composable<Route.Login> {
            ShowLoginScreen(context, navController, authViewModel)
        }
        composable<Route.Register> {
            ShowRegisterScreen(navController, authViewModel)
        }
        composable<Route.Invite> {
            ShowInviteScreen(navController)
        }
        composable<Route.NewEvent> {
            ShowNewEventScreen(context, navController, eventViewModel, gameViewModel, userViewModel)
        }
        composable<Route.Profile> {
            ShowProfileScreen(context, navController, userViewModel)
        }
        composable<Route.MyProfile> {
            ShowMyProfileScreen(
                navController, authViewModel, postViewModel,
                userViewModel, eventViewModel, gameViewModel
            )
        }
        composable<Route.EditMyProfile> {
            ShowEditMyProfile(context, navController, authViewModel, onThemeChange)
        }
        composable<Route.GameInfo> {
            ShowGameInfo(navController, gameViewModel, context)
        }
        composable<Route.NewPost> {
            ShowNewPostScreen(navController, postViewModel)
        }
        composable<Route.EventInfo> {
            ShowEventInfoScreen(context, navController, userViewModel, eventViewModel)
        }
        composable<Route.Gamification> {
            GamificationScreen(navController, userViewModel)
        }
        composable<Route.UtilTools> {
            ShowToolScreen(navController, triviaViewModel)
        }
        composable<Route.NotificationCenter> {
            ShowNotificationScreen(navController, notificationViewModel)
        }
    }
}
