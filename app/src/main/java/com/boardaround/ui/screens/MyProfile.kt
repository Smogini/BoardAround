package com.boardaround.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.ExpandableSection
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel,
    eventViewModel: EventViewModel,
    gameViewModel: GameViewModel
) {
    val myGames by gameViewModel.userGames.collectAsState(initial = emptyList())
    val myFriends by userViewModel.getFriends().collectAsState(initial = emptyList())
    val achievementList by userViewModel.achievementList.collectAsState()
    val myPosts by postViewModel.userPosts.collectAsState(initial = emptyList())
    val myEvents by eventViewModel.eventsFound.collectAsState(initial = emptyList())

    gameViewModel.getUserGames()
    postViewModel.getPostsByUsername()
    eventViewModel.searchEventsByUsername()

    ScreenTemplate(
        title = "My profile",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true
    ) {
        item {
            ExpandableSection(
                title = "My posts",
                icon = Icons.AutoMirrored.Default.Article,
                itemList = myPosts,
                labelProvider = { it.title },
                onItemClick = { postViewModel.selectPost(it) }
            )
        }

        item {
            ExpandableSection(
                title = "My events",
                icon = Icons.Default.Event,
                itemList = myEvents,
                labelProvider = { it.name },
                onItemClick = {
                    eventViewModel.selectEvent(it)
                    navController.navigateSingleTop(Route.EventInfo)
                }
            )
        }

        item {
            ExpandableSection(
                title = "My games",
                icon = Icons.Default.Extension,
                itemList = myGames,
                labelProvider = { it.name },
                onItemClick = {
                    gameViewModel.getGameInfo(it.gameId)
                    navController.navigateSingleTop(Route.GameInfo)
                }
            ) {
                CustomClickableIcon(
                    title = "Remove game",
                    icon = Icons.Filled.Delete,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = { gameViewModel.removeSavedGame(it.gameId) }
                )
            }
        }

        item {
            ExpandableSection(
                title = "My friends",
                icon = Icons.Default.Group,
                itemList = myFriends,
                labelProvider = { it.username },
                onItemClick = {
                    userViewModel.selectUser(it)
                    navController.navigateSingleTop(Route.Profile)
                },
                trailingIcon = {
                    CustomClickableIcon(
                        title = "Remove friend",
                        icon = Icons.Default.Delete,
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        onClick = { userViewModel.removeFriend(it.username) }
                    )
                }
            )
        }

        item {
            CustomButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigateSingleTop(Route.Login)
                },
                text = "Logout"
            )
        }
    }
}
