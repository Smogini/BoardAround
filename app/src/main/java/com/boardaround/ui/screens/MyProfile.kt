package com.boardaround.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.data.dao.UserDAO
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.PostItem
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.data.entities.Post
import com.boardaround.data.entities.User
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel // Aggiungi UserViewModel
) {
    val username = authViewModel.retrieveUsername()
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList<Post>())

    // Stato per memorizzare i dati dell'utente
    var user by remember { mutableStateOf<User?>(null) }

    // Quando la schermata si apre, carichiamo i dati dell'utente
    LaunchedEffect(username) {
        // Carica l'utente dal ViewModel
        userViewModel.getUserData(username) { fetchedUser ->
            user = fetchedUser // Aggiorna lo stato con l'utente recuperato
        }
        postViewModel.getPostsByUser()  // Recupera i post
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

            // Mostra le informazioni dell'utente se sono disponibili
            user?.let {
                Text(text = "Nome: ${it.name}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Text(text = "Email: ${it.email}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Text(text = "Data di Nascita: ${it.dob}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sezione "I miei post"
            ExpandableSection(
                title = "I miei post",
                posts = myPosts.value
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

