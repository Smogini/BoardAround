package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardaround.data.entities.toGame
import com.boardaround.data.getCurrentUser
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomItem
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
    userViewModel: UserViewModel = viewModel(),
    eventViewModel: EventViewModel,
    gameViewModel: GameViewModel
) {
    val user = LocalContext.current.getCurrentUser()
    val username = user.username

    val myGames by gameViewModel.userGames.collectAsState(initial = emptyList())
    val myFriends by userViewModel.getFriends(username).collectAsState(initial = emptyList())
    val myPosts by postViewModel.userPosts.collectAsState(initial = emptyList())
    val myEvents by eventViewModel.eventsFound.collectAsState(initial = emptyList())

    var showGames by remember { mutableStateOf(false) }
    var showEvents by remember { mutableStateOf(false) }
    var showPosts by remember { mutableStateOf(false) }
    var showFriends by remember { mutableStateOf(false) }

    gameViewModel.getUserGames(username)
    postViewModel.getPostsByUsername(username)
    eventViewModel.searchEventsByUsername(username)

    ScreenTemplate(
        title = "Profilo di $username",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 100.dp)
        ) {
            item {
                Text(
                    "I miei dati:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 30.sp)
                )

                user.let {
                    Text(
                        text = "Nome: ${it.name}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                    )
                    Text(
                        text = "Email: ${it.email}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                    )
                    Text(
                        text = "Data di Nascita: ${it.dob}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExpandableSection(
                    title = "I miei post",
                    items = myPosts,
                    itemContent = { post ->
                        CustomItem(
                            title = post.title,
                            description = post.content,
                            imageUrl = post.imageUri,
                        )
                    },
                    isExpanded = showPosts,
                    onExpandChange = { showPosts = !showPosts },
                    onItemClick = { selectedPost ->
                        postViewModel.selectPost(selectedPost)
//                        navController.navigateSingleTop(Route.Post)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExpandableSection(
                    title = "I miei eventi",
                    items = myEvents,
                    itemContent = { event ->
                        CustomItem(
                            title = event.name,
                            description = event.description,
                            imageUrl = event.imageUrl
                        )
                    },
                    isExpanded = showEvents,
                    onExpandChange = { showEvents = !showEvents },
                    onItemClick = { selectedEvent ->
                        eventViewModel.selectEvent(selectedEvent)
                        navController.navigateSingleTop(Route.EventInfo)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ExpandableSection(
                    title = "I miei giochi",
                    items = myGames,
                    isExpanded = showGames,
                    onExpandChange = { showGames = !showGames },
                    onItemClick = { selectedGame ->
                        gameViewModel.selectGame(selectedGame.toGame())
                        navController.navigateSingleTop(Route.GameInfo)
                    }
                ) { savedGame ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = savedGame.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 250.dp)
                        )
                        CustomButtonIcon(
                            title = "Rimuovi gioco",
                            icon = Icons.Filled.Delete,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            onClick = { gameViewModel.removeSavedGame(savedGame) },
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ExpandableSection(
                    title = "I miei amici:",
                    items = myFriends,
                    isExpanded = showFriends,
                    onExpandChange = { showFriends = !showFriends },
                    onItemClick = { selectedFriend ->
                        userViewModel.selectUser(selectedFriend)
                        navController.navigateSingleTop(Route.Profile)
                    }
                ) { friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = friend.username)
                        CustomButtonIcon(
                            title = "Rimuovi amico",
                            icon = Icons.Filled.Delete,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            onClick = { userViewModel.removeFriend(username, friend.username) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(180.dp))

                CustomButton(
                    onClick = {
                        authViewModel.logout()
                        navController.navigateSingleTop(Route.Login)
                    },
                    text = "Esci dal profilo"
                )

                CustomButton(
                    onClick = {
                        authViewModel.deleteUser(user)
                        navController.navigateSingleTop(Route.Login)
                    },
                    text = "Elimina il profilo"
                )
            }
        }
    }
}
