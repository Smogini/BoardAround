package com.boardaround.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.SearchResultCarousel
import com.boardaround.ui.components.SuggestedGamesCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel

data class SuggestedGame(
    val name: String,
    val imageResId: Int,
    val rating: Double
)

@Composable
fun ShowHomePageScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    gameViewModel: GameViewModel,
    eventViewModel: EventViewModel
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val games by gameViewModel.gamesFound.collectAsState()
    val users by userViewModel.usersFound.collectAsState()
    val events by eventViewModel.eventsFound.collectAsState()
    val focusManager = LocalFocusManager.current

    val suggestedGames = listOf(
        SuggestedGame(
            name = "Munchkin",
            imageResId = R.drawable.munchkin,
            rating = 6.2
        ),
        SuggestedGame(
            name = "Dixit",
            imageResId = R.drawable.dixit,
            rating = 7.3
        ),
        SuggestedGame(
            name = "Secret Hitler",
            imageResId = R.drawable.secrethitler,
            rating = 7.7
        ),
        SuggestedGame(
            name = "Bang!",
            imageResId = R.drawable.bang,
            rating = 6.6
        ),
        SuggestedGame(
            name = "Catan",
            imageResId = R.drawable.catan,
            rating = 7.2
        )
    )


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
                            iconColor = PrimaryText,
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
                    items = games.games ?: emptyList(),
                    onClick = { game ->
                        gameViewModel.selectGame(game)
                        navController.navigateSingleTop(Route.GameInfo)
                    },
                    imageUrlProvider = { it.imageUrl.toString() },
                    labelProvider = { it.nameElement.value },
                )

                SuggestedGamesCarousel(
                    games = suggestedGames,
                    onClick = { game ->
                        // Puoi fare qualcosa tipo mostrare un toast, log, dialog, ecc.
                        // Per ora lasciamolo vuoto o fai un log
                    }
                )
            }
        }
    }
}