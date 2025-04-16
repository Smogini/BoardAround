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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.boardaround.ui.components.GameRulesDialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.boardaround.viewmodel.TriviaViewModel
import com.boardaround.viewmodel.UserViewModel

data class SuggestedGame(
    val name: String,
    val imageResId: Int,
    val rating: Double,
    val rules: String
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
    val (selectedGame, onGameSelected) = remember { mutableStateOf<SuggestedGame?>(null) }
    val triviaViewModel: TriviaViewModel = viewModel()
    val triviaQuestions by triviaViewModel.questions.collectAsState()
    val isTriviaVisible = remember { mutableStateOf(false) }

    val suggestedGames = listOf(
        SuggestedGame(
            name = "Munchkin",
            imageResId = R.drawable.munchkin,
            rating = 6.2,
            rules = "Raggiungi il livello 10 sconfiggendo mostri e accumulando tesori. Usa carte per aiutarti o ostacolare gli altri giocatori."
        ),
        SuggestedGame(
            name = "Dixit",
            imageResId = R.drawable.dixit,
            rating = 7.3,
            rules = "A turno, un giocatore (il narratore) sceglie una carta dalla sua mano e fornisce un indizio (una parola, una frase, un suono) che la descrive. Gli altri giocatori scelgono una carta dalla loro mano che pensano si adatti all'indizio. Il narratore mescola la sua carta con quelle degli altri e le rivela. I giocatori votano quale carta pensano sia quella del narratore. I punti vengono assegnati in base a chi ha indovinato la carta del narratore e a chi ha ricevuto voti per la propria carta."
        ),
        SuggestedGame(
            name = "Secret Hitler",
            imageResId = R.drawable.secrethitler,
            rating = 7.7,
            rules = "I giocatori sono segretamente divisi in liberali e fascisti, con un giocatore che è segretamente Hitler. I fascisti conoscono l'identità di Hitler, mentre i liberali no. I giocatori eleggono un presidente e un cancelliere che collaborano per approvare leggi. I liberali vincono se approvano 5 leggi liberali o se Hitler viene assassinato. I fascisti vincono se approvano 6 leggi fasciste o se Hitler viene eletto cancelliere dopo l'approvazione di almeno 3 leggi fasciste."
        ),
        SuggestedGame(
            name = "Bang!",
            imageResId = R.drawable.bang,
            rating = 6.6,
            rules = "I giocatori assumono ruoli segreti (sceriffo, vice, fuorilegge, rinnegato) e cercano di eliminare gli avversari. Lo sceriffo e i vice devono eliminare i fuorilegge e il rinnegato, mentre i fuorilegge devono eliminare lo sceriffo. Il rinnegato deve eliminare tutti. I giocatori usano carte per attaccare, difendersi e usare abilità speciali."
        ),
        SuggestedGame(
            name = "Catan",
            imageResId = R.drawable.catan,
            rating = 7.2,
            rules = "I giocatori costruiscono insediamenti, città e strade su un'isola esagonale, raccogliendo risorse (legno, mattoni, pecora, grano, minerale) in base ai tiri di dado. L'obiettivo è raggiungere 10 punti vittoria costruendo strutture, ottenendo carte sviluppo e ottenendo la strada più lunga o l'esercito più grande."
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
                        onGameSelected(game)// Puoi fare qualcosa tipo mostrare un toast, log, dialog, ecc.
                        // Per ora lasciamolo vuoto o fai un log
                    }
                )

                if (selectedGame != null) {
                    GameRulesDialog(
                        gameName = selectedGame.name,
                        rules = selectedGame.rules,
                        onDismiss = { onGameSelected(null) } // Resetta lo stato quando il Dialog viene chiuso
                    )
                }

                if (isTriviaVisible.value) {
                    MiniTrivia(
                        questions = triviaQuestions,
                        onQuizFinished = { isTriviaVisible.value = false }
                    )
                } else {
                    Button(onClick = {
                        triviaViewModel.loadTriviaQuestions() // Carica le domande
                        isTriviaVisible.value = true // Mostra MiniTrivia
                    }) {
                        Text("Gioca a Trivia")
                    }
                }

            }
        }
    }
}