package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.Event
import com.boardaround.ui.components.MyEventsCarousel
import com.boardaround.ui.components.MyGamesCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.utils.GameSearchResult
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowHomePageScreen(navController: NavController, userViewModel: UserViewModel) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var games by remember { mutableStateOf(GameSearchResult(0, listOf())) }
    val focusManager = LocalFocusManager.current

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
        currentRoute = Route.Homepage,
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
                        onClick = {
                            userViewModel.searchGames(searchQuery.value.text) { result ->
                                games = result
                            }
//                            userViewModel.searchUsers(searchQuery.value.text) { userList ->
//                                if (userList.isEmpty()) {
//                                    Log.d("homepage", "nessun utente trovato")
//                                }
//                            }
                            focusManager.clearFocus()
                        }
                    )
                },
                trailingIcon = {
                    if (searchQuery.value.text.isNotEmpty()) {
                        CustomButtonIcon(
                            title = "Clear",
                            icon = Icons.Filled.Clear,
                            iconColor = Errors,
                            onClick = { searchQuery.value = TextFieldValue("") }
                        )
                    }
                },
            )

            MyGamesCarousel(title = "Giochi trovati: ${games.total}", searchResult = games, navController)
            MyEventsCarousel(events = myEvents, navController = navController)
        }
    }
}