package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.SearchResultCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowHomePageScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    gameViewModel: GameViewModel,
    eventViewModel: EventViewModel
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val searchGamesResult by gameViewModel.gamesFound.collectAsState()
    val users by userViewModel.usersFound.collectAsState()
    val events by eventViewModel.eventsFound.collectAsState()
    val focusManager = LocalFocusManager.current

    val suggestedGames by gameViewModel.suggestedGames.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.getSuggestedGames(count = 5)
    }

    ScreenTemplate(
        title = "Homepage",
        currentRoute = Route.Homepage,
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
                            iconColor = PrimaryBrown,
                            onClick = {
                                gameViewModel.searchGames(searchQuery.value.text)
                                userViewModel.searchUsers(searchQuery.value.text)
                                eventViewModel.searchEvents(searchQuery.value.text)
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
                    keyboardType = KeyboardType.Ascii
                )

                SearchResultCarousel(
                    title = "Eventi trovati",
                    items = events,
                    onClick = { event ->
                        eventViewModel.selectEvent(event)
                        navController.navigateSingleTop(Route.EventInfo)
                    },
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
                    items = searchGamesResult.games ?: emptyList(),
                    onClick = { game ->
                        gameViewModel.getGameInfo(game.id)
                        navController.navigateSingleTop(Route.GameInfo)
                    },
                    // TODO
                    imageUrlProvider = { "" },
                    labelProvider = { it.name.value },
                )

                SearchResultCarousel(
                    title = "Consigliati",
                    items = suggestedGames,
                    onClick = { game ->
                        gameViewModel.getGameInfo(game.id)
                        navController.navigateSingleTop(Route.GameInfo)
                    },
                    imageUrlProvider = { it.imageUrl.toString() },
                    labelProvider = { it.name }
                )

            }
        }
    }
}