package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomImageCard
import com.boardaround.ui.components.CustomTitle
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
    val myFriends by userViewModel.userFriends.collectAsState(initial = emptyList())
    val pendingFriends by userViewModel.pendingFriendships.collectAsState(initial = emptyList())
    val achievementList by userViewModel.achievementList.collectAsState()
    val myPosts by postViewModel.userPosts.collectAsState(initial = emptyList())
    val myEvents by eventViewModel.eventsFound.collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.getUserGames()
        postViewModel.getUserPosts()
        eventViewModel.fetchUserEvents()
        userViewModel.loadFriends()
    }

    ScreenTemplate(
        title = "My profile",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true
    ) {
        item {
            currentUser?.let { UserInfoCard(it) }
        }

        item{
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
                    navController.navigate(Route.EventInfo)
                },
                trailingIcon = {
                    CustomClickableIcon(
                        title = "Remove event",
                        icon = Icons.Default.Delete,
                        iconColor = MaterialTheme.colorScheme.error,
                        onClick = { eventViewModel.deleteEvent(it) }
                    )
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
                    navController.navigate(Route.GameInfo)
                },
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
                title = "My Friends",
                icon = Icons.Default.Groups,
                itemList = myFriends,
                labelProvider = { it.username },
                onItemClick = {
                    userViewModel.selectUser(it)
                    navController.navigate(Route.Profile)
                },
            ) {
                CustomClickableIcon(
                    title = "Remove friend",
                    icon = Icons.Filled.Delete,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = { userViewModel.removeFriend(it.username) }
                )
            }
        }

        item {
            ExpandableSection(
                title = "Pending Requests",
                icon = Icons.Default.Group,
                itemList = pendingFriends,
                labelProvider = { it.fromUserId },
                onItemClick = {
                    val user = userViewModel
//                    userViewModel.selectUser()
                    navController.navigate(Route.Profile)
                },
                trailingIcon = { friendship ->
                    Row {
                        CustomClickableIcon(
                            title = "Accept friend",
                            icon = Icons.Default.Group,
                            iconColor = MaterialTheme.colorScheme.primary,
                            onClick = { userViewModel.acceptFriendRequest(friendship) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomClickableIcon(
                            title = "Decline friend",
                            icon = Icons.Default.Delete,
                            iconColor = MaterialTheme.colorScheme.error,
                            onClick = { userViewModel.declineFriendRequest(friendship) }
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Route.Login)
                },
                text = "Logout"
            )
        }
    }
}

@Composable
fun UserInfoCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomImageCard(
                item = null,
                isSelected = true,
                image = user.profilePic.orEmpty(),
                contentDescription = "profile picture",
                cardSize = 72,
                cardShape = CircleShape
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                CustomTitle(
                    text = user.name,
                    textStyle = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                CustomTitle(
                    text = "@${user.username}",
                    textStyle = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                CustomTitle(
                    text = user.email,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))
                CustomTitle(
                    text = "Born on ${user.dob}",
                    textStyle = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
