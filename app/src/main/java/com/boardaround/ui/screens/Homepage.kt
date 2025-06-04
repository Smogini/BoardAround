package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.data.entities.Article
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomCarousel
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.ui.components.NewsArticleItem
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
    val users by userViewModel.usersFound.collectAsState(initial = emptyList())
    val events by eventViewModel.eventsFound.collectAsState(initial = emptyList())
    val focusManager = LocalFocusManager.current

    val suggestedGames by gameViewModel.suggestedGames.collectAsState()

    val articleList by userViewModel.articleList.collectAsState()

    ScreenTemplate(
        title = "Homepage",
        currentRoute = Route.Homepage,
        navController = navController,
    ) {
        item {
            CustomTextField(
                label = "What are you looking for?",
                value = searchQuery.value.text,
                onValueChange = { searchQuery.value = TextFieldValue(it) },
                leadingIcon = {
                    CustomClickableIcon(
                        title = "Search",
                        icon = Icons.Default.Search,
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
                        CustomClickableIcon(
                            title = "Clear",
                            icon = Icons.Default.Clear,
                            iconColor = MaterialTheme.colorScheme.error,
                            onClick = { searchQuery.value = TextFieldValue("") }
                        )
                    }
                },
                keyboardType = KeyboardType.Ascii,
                modifier = Modifier.padding(top = 10.dp)
            )

            CustomCarousel(
                title = "Events found",
                items = events,
                onClick = { event ->
                    eventViewModel.selectEvent(event)
                    navController.navigate(Route.EventInfo)
                },
                imageUrlProvider = { it.imageUrl },
                labelProvider = { it.name },
            )

            CustomCarousel(
                title = "Users found",
                items = users,
                onClick = { user ->
                    userViewModel.selectUser(user)
                    navController.navigate(Route.Profile)
                },
                imageUrlProvider = { it.profilePic.orEmpty() },
                labelProvider = { it.username },
            )

            CustomCarousel(
                title = "Game boards found",
                items = searchGamesResult.games ?: emptyList(),
                onClick = { game ->
                    gameViewModel.getGameInfo(game.id)
                    navController.navigate(Route.GameInfo)
                },
                imageUrlProvider = { "" },
                labelProvider = { it.name.value },
            )

            /* TODO: inserire la lista degli eventi cercati tramite indirizzo con funzione apposita */
            CustomCarousel(
                title = "Events nearby you",
                items = events,
                onClick = { event ->
                    eventViewModel.selectEvent(event)
                    navController.navigate(Route.EventInfo)
                },
                imageUrlProvider = { it.imageUrl },
                labelProvider = { it.name }
            )
        }

        item {
            CustomCarousel(
                title = "Suggested",
                items = suggestedGames,
                onClick = { game ->
                    gameViewModel.getGameInfo(game.id)
                    navController.navigate(Route.GameInfo)
                },
                imageUrlProvider = { it.imageUrl.toString() },
                labelProvider = { it.name },
                trailingIcon = {
                    CustomClickableIcon(
                        title = "refresh",
                        icon = Icons.Default.Refresh,
                        onClick = { gameViewModel.getSuggestedGames(10) }
                    )
                }
            )
        }

        item {
            NewsSection(
                articles = articleList,
                trailingIcon = {
                    CustomClickableIcon(
                        title = "refresh",
                        icon = Icons.Default.Refresh,
                        onClick = { userViewModel.fetchBoardGameNews() }
                    )
                }
            )
        }
    }
}

@Composable
private fun NewsSection(
    articles: List<Article>,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTitle(
                text = "News",
                textStyle = MaterialTheme.typography.headlineMedium,
                alignment = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            trailingIcon?.invoke()
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (articles.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTitle(
                    text = "Loading news...",
                    textStyle = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(articles) { article ->
                    val articleUrl = article.url.toString()

                    NewsArticleItem(
                        article = article,
                        onClick = { uriHandler.openUri(articleUrl) },
                    )

                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
