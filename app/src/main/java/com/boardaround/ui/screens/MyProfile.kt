package com.boardaround.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.EventItem
import com.boardaround.ui.components.PostItem
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
    val user by remember { mutableStateOf(userViewModel.getCurrentUser()) }
    val username = user?.username ?: ""

    val myGames = gameViewModel.getUserGames(username).collectAsState(initial = emptyList())
    val myFriends = userViewModel.getFriends(username).collectAsState(initial = emptyList())
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList())
    val myEvent = eventViewModel.eventsFound.collectAsState(initial = emptyList())

    var showGames by remember { mutableStateOf(false) }
    var showEvents by remember { mutableStateOf(false) }
    var showPosts by remember { mutableStateOf(false) }
    var showFriends by remember { mutableStateOf(false) }

    postViewModel.getPostsByUser()
    // eventViewModel.getEventsByUser()

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

                user?.let {
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
                    items = myPosts.value,
                    itemContent = { post ->
                        PostItem(post)
                    },
                    isExpanded = showPosts,
                    onExpandChange = { showPosts = !showPosts }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExpandableSection(
                    title = "I miei eventi",
                    items = myEvent.value,
                    itemContent = { event ->
                        EventItem(event)
                    },
                    isExpanded = showEvents,
                    onExpandChange = { showEvents = !showEvents }
                )

                Spacer(modifier = Modifier.height(24.dp))

                /* TODO: formattare correttamente la lista visualizzata */
                ExpandableSection(
                    title = "I miei giochi",
                    items = myGames.value,
                    isExpanded = showGames,
                    onExpandChange = { showGames = !showGames }
                ) { game ->
                    Log.d("myprofile", "${ myGames.value }")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = game)
                        CustomButtonIcon(
                            title = "Rimuovi gioco",
                            icon = Icons.Filled.Delete,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            onClick = { gameViewModel.removeGame(username, game) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ExpandableSection(
                    title = "I miei amici:",
                    items = myFriends.value,
                    isExpanded = showFriends,
                    onExpandChange = { showFriends = !showFriends }
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
            }
        }
    }
}

@Composable
fun <T> ExpandableSection(
    title: String,
    items: List<T>,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    emptyMessage: String = "Nessun elemento disponibile",
    itemContent: @Composable (T) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange(!isExpanded) }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = if (isExpanded) "Mostra meno" else "Mostra di più"
            )
        }

        if (isExpanded) {
            if (items.isNotEmpty()) {
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    items(items) { item ->
                        itemContent(item)
                    }
                }
            } else {
                Text(
                    text = emptyMessage,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
