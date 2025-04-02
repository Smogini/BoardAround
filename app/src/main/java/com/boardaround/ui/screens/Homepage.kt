package com.boardaround.ui.screens

// ... altre importazioni ...
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.Event
import com.boardaround.ui.components.MyEventsCarousel
import com.boardaround.ui.components.MyGamesCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.utils.Game // Assicurati che il percorso sia corretto
import com.boardaround.viewmodel.GameViewModel // Importa GameViewModel
import com.boardaround.viewmodel.UserViewModel

class Homepage(private val navController: NavController) {

    @Composable
    fun ShowHomePageScreen(userViewModel: UserViewModel) {
        if (userViewModel.isUserLoggedIn()) {
            val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

            // Ottieni un'istanza del GameViewModel
            val gameViewModel: GameViewModel = viewModel()

            // Osserva il StateFlow dei giochi
            val games by gameViewModel.games.collectAsState()

            val myEvents = remember {
                listOf(
                    Event("Evento 1", "url_immagine_evento_1"),
                    Event("Evento 2", "url_immagine_evento_2"),
                    Event("Evento 3", "url_immagine_evento_3"),
                    Event("Evento 4", "url_immagine_evento_4")
                )
            }

            ScreenTemplate(
                title = "Homepage",
                showBottomBar = true,
                navController = navController,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CustomTextField(
                        label = "Cosa stai cercando?",
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        leadingIcon = {
                            CustomButtonIcon(
                                title = "Search",
                                icon = Icons.Filled.Search,
                                iconColor = PrimaryText,
                                onClick = { /* TODO */ }
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.value.text.isNotEmpty()) {
                                CustomButtonIcon(
                                    title = "Clear",
                                    icon = Icons.Filled.Clear,
                                    iconColor = Errors,
                                    onClick = { searchQuery.value =
                                        TextFieldValue("") }
                                )
                            }
                        },
                    )

                    // Passa la lista dei giochi dal ViewModel al carosello
                    if (games.isNotEmpty()) {
                        MyGamesCarousel(games = games)
                    } else {
                        Text("Caricamento giochi...") // O un indicatore di caricamento
                    }

                    MyEventsCarousel(events = myEvents)
                }
            }
        } else {
            navController.navigate(Route.Login) {
                launchSingleTop = true
            }
        }
    }
}