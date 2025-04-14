package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.boardaround.data.entities.Event
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.SearchResultCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowHomePageScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    gameViewModel: GameViewModel
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val games by gameViewModel.gamesFound.collectAsState()
    val users by userViewModel.usersFound.collectAsState()
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    val focusManager = LocalFocusManager.current

    ScreenTemplate(
        title = "Homepage",
        currentRoute = Route.Homepage,
        showBottomBar = true,
        navController = navController,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
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
                                gameViewModel.searchGames(searchQuery.value.text)
                                userViewModel.searchUsers(searchQuery.value.text)
                                userViewModel.searchEvent(searchQuery.value.text) { eventsList ->
                                    events = eventsList
                                }
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

                SearchResultCarousel(
                    title = "I miei eventi",
                    items = events,
//                    onClick = { navController.navigateSingleTop(Route.) },
                    imageUrlProvider = { it.imageUrl.toString() },
                    labelProvider = { it.name },
                )
                SearchResultCarousel(
                    title = "Utenti trovati",
                    items = users,
                    onClick = { user ->
                        userViewModel.selectUser(user)
                        navController.navigateSingleTop(Route.Profile)
                    },
                    imageUrlProvider = { it.profilePic },
                    labelProvider = { it.username },
                )
                SearchResultCarousel(
                    title = "Giochi trovati",
                    items = games.games ?: emptyList(),
                    onClick = { game ->
                        gameViewModel.selectGame(game)
                        navController.navigateSingleTop(Route.GameInfo)
                    },
                    imageUrlProvider = { it.imageUrl.toString() },
                    labelProvider = { it.nameElement.value },
                )
            }
        }
    }
}