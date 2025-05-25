package com.boardaround.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomCarousel
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.NewsArticleItem
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.NewsViewModel
import com.boardaround.viewmodel.UserViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun ShowHomePageScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    gameViewModel: GameViewModel,
    eventViewModel: EventViewModel,
    newsViewModel: NewsViewModel
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val searchGamesResult by gameViewModel.gamesFound.collectAsState()
    val users by userViewModel.usersFound.collectAsState()
    val events by eventViewModel.eventsFound.collectAsState()
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val suggestedGames by gameViewModel.suggestedGames.collectAsState()
    val searchEventsByAddress by eventViewModel.eventsFound.collectAsState()

    val newsState by newsViewModel.newsUiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
//            if (isGranted) {
//                getUserLocation(fusedLocationClient, eventViewModel)
//            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
//        } else {
//            getUserLocation(fusedLocationClient, eventViewModel)
//        }
    }

    LaunchedEffect(Unit) {
        gameViewModel.getSuggestedGames(count = 5)
    }

    ScreenTemplate(
        title = "Homepage",
        currentRoute = Route.Homepage,
        navController = navController,
    ) {
        item {
            CustomTextField(
                label = "What are you looking for?",
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                leadingIcon = {
                    CustomClickableIcon(
                        title = "Search",
                        icon = Icons.Default.Search,
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
                        CustomClickableIcon(
                            title = "Clear",
                            icon = Icons.Default.Clear,
                            iconColor = Errors,
                            onClick = { searchQuery.value = TextFieldValue("") }
                        )
                    }
                },
                keyboardType = KeyboardType.Ascii
            )

            CustomCarousel(
                title = "Events found",
                items = events,
                onClick = { event ->
                    eventViewModel.selectEvent(event)
                    navController.navigateSingleTop(Route.EventInfo)
                },
                imageUrlProvider = { it.imageUrl },
                labelProvider = { it.name },
            )

            CustomCarousel(
                title = "Users found",
                items = users,
                onClick = { user ->
                    userViewModel.selectUser(user)
                    navController.navigateSingleTop(Route.Profile)
                },
                imageUrlProvider = { it.profilePic },
                labelProvider = { it.username },
            )

            CustomCarousel(
                title = "Game boards found",
                items = searchGamesResult.games ?: emptyList(),
                onClick = { game ->
                    gameViewModel.getGameInfo(game.id)
                    navController.navigateSingleTop(Route.GameInfo)
                },
                imageUrlProvider = { "" },
                labelProvider = { it.name.value },
            )

            CustomCarousel(
                title = "Suggested",
                items = suggestedGames,
                onClick = { game ->
                    gameViewModel.getGameInfo(game.id)
                    navController.navigateSingleTop(Route.GameInfo)
                },
                imageUrlProvider = { it.imageUrl.toString() },
                labelProvider = { it.name }
            )

            CustomCarousel(
                title = "Events nearby you",
                items = searchEventsByAddress,
                onClick = { event ->
                    eventViewModel.selectEvent(event)
                    navController.navigateSingleTop(Route.EventInfo)
                },
                imageUrlProvider = { it.imageUrl },
                labelProvider = { it.name }
            )
        }

        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "News",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                when {
                    newsState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    newsState.error != null -> {
                        Text(
                            text = "Error uploading news: ${newsState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    newsState.articles.isEmpty() -> {
                        Text(
                            text = "No news found.",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    else -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(newsState.articles) { article ->
                                NewsArticleItem(
                                    article = article,
                                    onClick = {
                                        article.url?.let { url ->
                                            try {
                                                uriHandler.openUri(url)
                                            } catch (e: Exception) {
                                                throw e
                                            }
                                        }
                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

//@SuppressLint("MissingPermission")
//fun getUserLocation(
//    fusedLocationClient: FusedLocationProviderClient,
//    eventViewModel: EventViewModel
//) {
//    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//        location?.let {
//            val latitude = it.latitude
//            val longitude = it.longitude
//
//            NominatimClient.instance.reverse(
//                lat = latitude.toString(),
//                lon = longitude.toString()
//            ).enqueue(object : retrofit2.Callback<StreetMapApiResponse> {
//                override fun onResponse(
//                    call: retrofit2.Call<StreetMapApiResponse>,
//                    response: retrofit2.Response<StreetMapApiResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val address = response.body()?.displayName
//                        if (!address.isNullOrBlank()) {
//                            eventViewModel.searchEventsByAddress(address)
//                        }
//                    } else {
//                        Log.e("getUserLocation", "Reverse geocoding fallita: ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: retrofit2.Call<StreetMapApiResponse>, t: Throwable) {
//                    Log.e("getUserLocation", "Errore nella reverse geocoding", t)
//                }
//            })
//        }
//    }
//}

