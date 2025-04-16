package com.boardaround.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.Post
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.EventItem
import com.boardaround.ui.components.PostItem
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ShowMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel = viewModel(),
    eventViewModel: EventViewModel
) {
    val username = authViewModel.retrieveUsername()
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList())
    val myEvent = eventViewModel.eventsFound.collectAsState(initial = emptyList())

    var user by remember { mutableStateOf<User?>(null) }


    var myGames by remember { mutableStateOf(listOf("Carcassonne", "7 Wonders", "Ticket to Ride")) }
    val myFriends by userViewModel.getFriends(username).collectAsState(initial = emptyList())

    var showGames by remember { mutableStateOf(false) }
    var showFriends by remember { mutableStateOf(false) }

    // Quando la schermata si apre, carichiamo i dati dell'utente
    LaunchedEffect(username) {
        // Carica l'utente dal ViewModel
        userViewModel.getUserData(username) { fetchedUser ->
            user = fetchedUser // Aggiorna lo stato con l'utente recuperato
        }
        postViewModel.getPostsByUser()
        // eventViewModel.getEventsByUser()
    }

    fun removeGame(game: String) {
        myGames = myGames.filter { it != game }
        // In futuro, qui dovrai anche comunicare con il backend per rimuovere il gioco dall'utente
    }

    ScreenTemplate(
        title = "Profilo di $username",
        currentRoute = Route.MyProfile,
        navController = navController,
        showBottomBar = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text("I miei dati:", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 30.sp))

            user?.let {
                Text(text = "Nome: ${it.name}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Text(text = "Email: ${it.email}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Text(text = "Data di Nascita: ${it.dob}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExpandableSection(
                title = "I miei post",
                posts = myPosts.value
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExpandableSectionForEvents(
                title = "I miei eventi",
                events = myEvent.value
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showGames = !showGames },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("I miei giochi:", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Icon(
                    imageVector = if (showGames) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (showGames) "Mostra meno" else "Mostra di più"
                )
            }
            if (showGames) {
                if (myGames.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) { // Altezza massima per la lista
                        items(myGames) { game ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = game)
                                IconButton(onClick = { removeGame(game) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Rimuovi gioco")
                                }
                            }
                        }
                    }
                } else {
                    Text("Nessun gioco disponibile", modifier = Modifier.padding(start = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableFriendsSection(
                friends = myFriends,
                showFriends = showFriends,
                onShowFriendsChange = { showFriends = it },
                onRemoveFriend = { friendUsername ->
                    userViewModel.removeFriend(username, friendUsername)
                }
            )

            Spacer(modifier = Modifier.height(250.dp))

            CustomButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Route.Login) {
                        launchSingleTop = true
                    }
                },
                text = "Esci dal profilo"
            )
        }
    }
}

@Composable
fun ExpandableFriendsSection(
    friends: List<User>,
    showFriends: Boolean,
    onShowFriendsChange: (Boolean) -> Unit,
    onRemoveFriend: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowFriendsChange(!showFriends) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("I miei amici:", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
        Icon(
            imageVector = if (showFriends) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
            contentDescription = if (showFriends) "Mostra meno" else "Mostra di più"
        )
    }
    if (showFriends) {
        if (friends.isNotEmpty()) {
            LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                items(friends) { friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = friend.username)
                        IconButton(onClick = { onRemoveFriend(friend.username) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Rimuovi amico")
                        }
                    }
                }
            }
        } else {
            Text("Nessun amico disponibile", modifier = Modifier.padding(start = 16.dp))
        }
    }
}





@Composable
fun ExpandableSection(
    title: String,
    posts: List<Post>
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Card che può essere espansa
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }, // Gestione del clic per espandere o rimpicciolire la sezione
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titolo della sezione
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mostra i post se la sezione è espansa
            if (isExpanded) {
                // Sezione espansa con lista di post
                LazyColumn {
                    items(posts) { post: Post ->
                        PostItem(post = post) // Usa PostItem per ogni post
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableSectionForEvents(
    title: String,
    events: List<Event>
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Card che può essere espansa
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }, // Gestione del clic per espandere o rimpicciolire la sezione
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titolo della sezione
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mostra i post se la sezione è espansa
            if (isExpanded) {
                // Sezione espansa con lista di post
                LazyColumn {
                    items(events) { event: Event ->
                        EventItem(event = event) // Usa PostItem per ogni post
                    }
                }
            }
        }
    }
}
