package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.boardaround.R
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.ExpandableSection
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.FriendsViewModel
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
    friendsViewModel: FriendsViewModel,
    gameViewModel: GameViewModel
) {
    val myGames by gameViewModel.userGames.collectAsState(initial = emptyList())
    val achievementList by userViewModel.achievementList.collectAsState()
    val myPosts by postViewModel.userPosts.collectAsState(initial = emptyList())
    val myEvents by eventViewModel.eventsFound.collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()
    val currentUsername = currentUser?.username ?: ""
    val myFriends by friendsViewModel.friends.collectAsState()



    gameViewModel.getUserGames()
    postViewModel.getPostsByUsername()
    eventViewModel.searchEventsByUsername()
    friendsViewModel.loadFriends(currentUsername)

        LaunchedEffect(currentUser?.uid) {
            val currentUserId = currentUser?.uid
            if (!currentUserId.isNullOrBlank()) {
                println(">>> Calling loadPendingFriends for userId: $currentUserId")
                friendsViewModel.loadFriends(currentUser?.username ?: "")
            } else {
                println(">>> currentUser is null or uid is blank")
            }
        }

        LaunchedEffect(currentUsername) {
                friendsViewModel.loadFriends(currentUsername)
        }




        ScreenTemplate(
            title = "My profile",
            currentRoute = Route.MyProfile,
            navController = navController,
            showBottomBar = true
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    val profileImagePath = currentUser?.profilePic
                    val imagePainter = rememberAsyncImagePainter(
                        model = profileImagePath ?: R.drawable.default_profile
                    )

                    Image(
                        painter = imagePainter,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    androidx.compose.material3.Text(
                        text = currentUsername,
                        style = MaterialTheme.typography.titleLarge
                        )
                }

                Spacer(modifier = Modifier.height(10.dp))
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
                        navController.navigateSingleTop(Route.EventInfo)
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
                        navController.navigateSingleTop(Route.GameInfo)
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

                    },
                ) {
                    CustomClickableIcon(
                        title = "Remove friend",
                        icon = Icons.Filled.Delete,
                        iconColor = MaterialTheme.colorScheme.error,
                        onClick = { friendsViewModel.removeFriend(currentUsername , it.username) }
                    )
                }
            }

            item {
                ExpandableSection(
                    title = "Pending Friend Requests",
                    icon = Icons.Default.Group,
                    itemList = friendsViewModel.pendingFriends.value,
                    labelProvider = {
                        it.fromUserId
                    },
                    onItemClick = { /*TODO*/ },
                    trailingIcon = { friendship ->
                        Row {
                            CustomClickableIcon(
                                title = "Accept friend",
                                icon = Icons.Default.Group,
                                iconColor = MaterialTheme.colorScheme.primary,
                                onClick = {
                                    friendsViewModel.acceptFriend(friendship)
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            CustomClickableIcon(
                                title = "Decline friend",
                                icon = Icons.Default.Delete,
                                iconColor = MaterialTheme.colorScheme.error,
                                onClick = {
                                    friendsViewModel.declineFriend(friendship)
                                }
                            )
                        }
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

