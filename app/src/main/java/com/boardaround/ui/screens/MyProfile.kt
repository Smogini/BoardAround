package com.boardaround.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.PostItem
import com.boardaround.viewmodel.AuthViewModel
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.data.entities.Post

@Composable
fun ShowMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel // <-- aggiunto
) {
    val username = authViewModel.retrieveUsername()
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList<Post>())


    // Quando la schermata si apre, carichiamo i post dell'utente
    LaunchedEffect(Unit) {
        postViewModel.getPostsByUser()
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
            CustomButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Route.Login) {
                        launchSingleTop = true
                    }
                },
                text = "Esci dal profilo"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sezione "I miei post"
            LazyColumn {
                items(myPosts.value) { post: Post ->
                    PostItem(post = post)
                }
            }
        }
    }
}
