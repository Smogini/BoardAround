package com.boardaround.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.data.entities.Post
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.EventItem
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.ui.components.PostItem
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel,
    eventViewModel: EventViewModel
) {
    val username = authViewModel.retrieveUsername()
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList())
    val myEvent = eventViewModel.eventsFound.collectAsState(initial = emptyList())

    var user by remember { mutableStateOf<User?>(null) }

    // Quando la schermata si apre, carichiamo i dati dell'utente
    LaunchedEffect(username) {
        // Carica l'utente dal ViewModel
        userViewModel.getUserData(username) { fetchedUser ->
            user = fetchedUser // Aggiorna lo stato con l'utente recuperato
        }
        postViewModel.getPostsByUser()
//        eventViewModel.getEventsByUser()
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
