package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.SearchResultCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.utils.GameSearchResult
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowHomePageScreen(navController: NavController, userViewModel: UserViewModel) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var games by remember { mutableStateOf(GameSearchResult(0, listOf())) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
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
                                userViewModel.searchGames(searchQuery.value.text) { result ->
                                    games = result
                                }
                                userViewModel.searchUsers(searchQuery.value.text) { userList ->
                                    users = userList
                                }
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
                    onClick = { navController.navigateSingleTop(Route.Profile) },
                    imageUrlProvider = { it.profilePic },
                    labelProvider = { it.username },
                )
                SearchResultCarousel(
                    title = "Giochi trovati",
                    items = games.games ?: emptyList(),
                    onClick = { navController.navigateSingleTop(Route.GameInfo) },
                    imageUrlProvider = { it.imageUrl.toString() },
                    labelProvider = { it.nameElement.value },
                )
            }
        }
    }
}