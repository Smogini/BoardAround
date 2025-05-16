package com.boardaround.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.network.NominatimClient
import com.boardaround.network.StreetMapApiResponse
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.SearchResultCarousel
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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

    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val suggestedGames by gameViewModel.suggestedGames.collectAsState()
    val searchEventsByAddress by eventViewModel.eventsFound.collectAsState()

    // Controlla i permessi di localizzazione
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Lancia una richiesta di permesso se non abbiamo il permesso
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Ottieni la posizione se il permesso è stato concesso
                getUserLocation(fusedLocationClient, eventViewModel)
            }
        }
    )

    // Se non c'è il permesso, richiedilo
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Ottieni la posizione se i permessi sono già stati concessi
            getUserLocation(fusedLocationClient, eventViewModel)
        }
        Log.d("homepage", "utente loggato: ${userViewModel.getUsername()}")
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
                label = "Cosa stai cercando?",
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

            SearchResultCarousel(
                title = "Eventi intorno a te",
                items = searchEventsByAddress,
                onClick = { event ->
                    eventViewModel.selectEvent(event)
                    navController.navigateSingleTop(Route.EventInfo)
                },
                imageUrlProvider = { it.imageUrl ?: "" },
                labelProvider = { it.name }
            )
        }
    }
}

@SuppressLint("MissingPermission")
fun getUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    eventViewModel: EventViewModel
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            val latitude = it.latitude
            val longitude = it.longitude

            // Reverse geocoding: converte coordinate in indirizzo
            NominatimClient.instance.reverse(
                lat = latitude.toString(),
                lon = longitude.toString()
            ).enqueue(object : retrofit2.Callback<StreetMapApiResponse> {
                override fun onResponse(
                    call: retrofit2.Call<StreetMapApiResponse>,
                    response: retrofit2.Response<StreetMapApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val address = response.body()?.displayName
                        if (!address.isNullOrBlank()) {
                            eventViewModel.searchEventsByAddress(address)
                        }
                    } else {
                        Log.e("getUserLocation", "Reverse geocoding fallita: ${response.code()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<StreetMapApiResponse>, t: Throwable) {
                    Log.e("getUserLocation", "Errore nella reverse geocoding", t)
                }
            })
        }
    }
}

