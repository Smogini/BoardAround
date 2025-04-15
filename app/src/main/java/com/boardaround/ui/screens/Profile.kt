package com.boardaround.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val userToShow by userViewModel.selectedUser.collectAsState()

    ScreenTemplate(
        title = "Profilo di ${userToShow?.username}",
        currentRoute = Route.Profile,
        navController
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                // Foto profilo
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(userToShow?.profilePic) // URL dell'immagine
                            .placeholder(R.drawable.default_profile) // Placeholder in caso di caricamento
                            .error(R.drawable.default_profile) // Placeholder in caso di errore
                            .build()
                    ),
                    contentDescription = "Foto profilo",
                    modifier = Modifier
                        .size(100.dp) // Dimensione dell'immagine
                        .clip(CircleShape), // Forma circolare
                    contentScale = ContentScale.Crop // Adatta l'immagine al contenitore
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Griglia per Nome, Email e Data di nascita
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Spaziatura tra le colonne
                ) {
                    Column(Modifier.weight(1f)) { // Nome
                        Text(
                            text = "Nome:",
                            style = MaterialTheme.typography.titleMedium, // Stile per l'etichetta
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userToShow?.name ?: "", // Mostra il nome o una stringa vuota se null
                            style = MaterialTheme.typography.headlineSmall, // Stile per il valore
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(Modifier.weight(1f)) { // Email
                        Text(
                            text = "Email:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userToShow?.email ?: "",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(Modifier.weight(1f)) { // Data di nascita
                        Text(
                            text = "Data di nascita:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userToShow?.dob ?: "",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    CustomButton(
                        onClick = {
                            navController.navigate(Route.Login) {
                                launchSingleTop = true
                            }
                        },
                        text = "Invia richiesta di amicizia"
                    )

                }
            }
        }
    }
}