package com.boardaround.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.boardaround.R
import com.boardaround.data.entities.Achievement
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
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
//    val myFriends by userViewModel.getFriends().collectAsState(initial = emptyList())
    val achievementList by userViewModel.achievementList.collectAsState()
    val myPosts by postViewModel.userPosts.collectAsState(initial = emptyList())
    val myEvents by eventViewModel.eventsFound.collectAsState(initial = emptyList())

    var showGames by remember { mutableStateOf(false) }
    var showEvents by remember { mutableStateOf(false) }
    var showPosts by remember { mutableStateOf(false) }
    var showFriends by remember { mutableStateOf(false) }

    gameViewModel.getUserGames()
    postViewModel.getPostsByUsername()
    eventViewModel.searchEventsByUsername()

    ScreenTemplate(
        title = "Il mio profilo",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true
    ) {
//        item {
//            ProfileHeader(user, achievementList)
//        }

        item {
            ProfileCard {
//                ExpandableSection(
//                    title = "I miei post",
//                    itemList = myPosts,
//                    itemContent = { post ->
//                        CustomItem(
//                            title = post.title,
//                            description = post.content,
//                            imageUrl = post.imageUri,
//                        )
//                    },
//                    isExpanded = showPosts,
//                    onExpandChange = { showPosts = !showPosts },
//                    onItemClick = {
//                        postViewModel.selectPost(it)
//                    }
//                )
            }
        }

        item {
            ProfileCard {
//                ExpandableSection(
//                    title = "I miei eventi",
//                    itemList = myEvents,
//                    itemContent = { event ->
//                        CustomItem(
//                            title = event.name,
//                            description = event.description,
//                            imageUrl = event.imageUrl
//                        )
//                    },
//                    isExpanded = showEvents,
//                    onExpandChange = { showEvents = !showEvents },
//                    onItemClick = {
//                        eventViewModel.selectEvent(it)
//                        navController.navigateSingleTop(Route.EventInfo)
//                    }
//                )
            }
        }

        item {
            ProfileCard {
//                ExpandableSection(
//                    title = "I miei giochi",
//                    itemList = myGames,
//                    isExpanded = showGames,
//                    onExpandChange = { showGames = !showGames },
//                    onItemClick = { game ->
//                        gameViewModel.getGameInfo(game.gameId)
//                        navController.navigateSingleTop(Route.GameInfo)
//                    }
//                ) { savedGame ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = savedGame.name,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                        CustomButtonIcon(
//                            title = "Rimuovi gioco",
//                            icon = Icons.Filled.Delete,
//                            iconColor = MaterialTheme.colorScheme.error,
//                            onClick = { gameViewModel.removeSavedGame(savedGame.gameId) }
//                        )
//                    }
//                }
            }
        }

        item {
            ProfileCard {
//                ExpandableSection(
//                    title = "I miei amici",
//                    itemList = myFriends,
//                    isExpanded = showFriends,
//                    onExpandChange = { showFriends = !showFriends },
//                    onItemClick = {
//                        userViewModel.selectUser(it)
//                        navController.navigateSingleTop(Route.Profile)
//                    }
//                ) { friend ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(friend.username)
//                        CustomButtonIcon(
//                            title = "Rimuovi amico",
//                            icon = Icons.Filled.Delete,
//                            iconColor = MaterialTheme.colorScheme.tertiary,
//                            onClick = {
//                                userViewModel.removeFriend(username, friend.username)
//                            }
//                        )
//                    }
//                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigateSingleTop(Route.Login)
                },
                text = "Esci dal profilo"
            )
        }
    }
}

@Composable
fun ProfileHeader(user: User, achievementList: List<Achievement>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Spazio tra il nome e l'immagine
            ) {
                Text(
                    text = "Ciao, ${user.name}!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )

                val profileImage = user.profilePic.takeIf { it.isNotEmpty() } ?: "android.resource://com.boardaround/${R.drawable.default_profile}"

                Image(
                    painter = rememberAsyncImagePainter(profileImage),
                    contentDescription = "Immagine profilo",
                    modifier = Modifier
                        .size(50.dp) // Imposta la dimensione dell'immagine
                        .clip(CircleShape) // Ritaglia l'immagine a forma di cerchio
                )
            }

            val numUnlocked = achievementList.count { it.isUnlocked }

            AnimatedVisibility(visible = expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary)
                    Text("Data di nascita: ${user.dob}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary)
                    Text(text = "Obiettivi raggiunti: $numUnlocked su ${achievementList.size}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@Composable
fun ProfileCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
