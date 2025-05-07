package com.boardaround.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.data.getCurrentUser
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val userToShow by userViewModel.selectedUser.collectAsState()
    val currentUserUsername = LocalContext.current.getCurrentUser().username
    val myFriends by userViewModel.getFriends(currentUserUsername).collectAsState(initial = emptyList())
    val isFriend = remember(myFriends, userToShow) {
        myFriends.any { it.username == userToShow?.username }
    }

    Log.d("profile", "To show: $userToShow")

    // Header della schermata
    ScreenTemplate(
        title = "Profilo di ${userToShow?.username}",
        currentRoute = Route.Profile,
        navController
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp) // Maggiore spaziatura tra gli elementi
        ) {
            item {
                // Immagine profilo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), // Aggiungi un po' di padding attorno
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(userToShow?.profilePic) // URL dell'immagine
                                .placeholder(R.drawable.default_profile) // Placeholder
                                .error(R.drawable.default_profile) // Placeholder per errore
                                .build()
                        ),
                        contentDescription = "Foto profilo",
                        modifier = Modifier
                            .size(120.dp) // Dimensione dell'immagine
                            .clip(CircleShape) // Forma circolare
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape), // Aggiungi bordo
                        contentScale = ContentScale.Crop
                    )
                }

                // Card con informazioni principali
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = userToShow?.name ?: "Nome non disponibile",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = userToShow?.email ?: "Email non disponibile",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = userToShow?.dob ?: "Data di nascita non disponibile",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottone per aggiungere o rimuovere amico
                if (userToShow != null && userToShow!!.username != currentUserUsername) {
                    if (isFriend) {
                        CustomButton(
                            onClick = {
                                userViewModel.removeFriend(currentUserUsername, userToShow!!.username)
                            },
                            text = "Rimuovi amico",
                        )
                    } else {
                        CustomButton(
                            onClick = {
                                userViewModel.addFriend(currentUserUsername, userToShow!!.username)
                            },
                            text = "Aggiungi amico",
                        )
                    }
                }
            }
        }
    }
}
