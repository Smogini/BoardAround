package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Game
import com.boardaround.data.entities.GameSearchResult
import com.boardaround.data.entities.SavedGame
import com.boardaround.data.entities.toGame
import com.boardaround.data.repositories.GameRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.network.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
    userRepository: UserRepository
): ViewModel() {

    private val _errorMessage = MutableStateFlow("")

    private var _gamesFound = MutableStateFlow(GameSearchResult(0, emptyList()))
    val gamesFound: StateFlow<GameSearchResult> = _gamesFound

    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame: StateFlow<Game?> = _selectedGame

    private val _userGames = MutableStateFlow<List<SavedGame>>(emptyList())
    val userGames: StateFlow<List<SavedGame>> = _userGames

    private val _suggestedGames = MutableStateFlow<List<Game>>(mutableListOf())
    val suggestedGames: StateFlow<List<Game>> = _suggestedGames

    private val currentUser = userRepository.currentUser

    init {
        getSuggestedGames(10)
    }

    fun getGameInfo(gameID: Int) {
        viewModelScope.launch {
            try {
                _selectedGame.value = ApiService.gameApi.getGameInfo(gameID).game.toGame()
            } catch(e: Exception) {
                _errorMessage.value = "Error in API call for searching games info ${e.message}"
            }
        }
    }

    fun searchGames(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _gamesFound.value = ApiService.gameApi.searchGames(query)
            } catch (e: Exception) {
                _errorMessage.value = "Error in API call for searching games ${e.message}"
            }
        }
    }

    fun saveGame(gameId: Int, gameName: String, imageUrl: String) {
        viewModelScope.launch {
            try {
                val savedGame = SavedGame(
                    userId = currentUser.firstOrNull()?.uid.orEmpty(),
                    gameId = gameId,
                    name = gameName,
                    imageUrl = imageUrl
                )
                gameRepository.addGame(savedGame)
            } catch (e: Exception) {
                _errorMessage.value = "Error saving the game $gameName: ${e.message}"
            }
        }
    }

    fun getUserGames() {
        viewModelScope.launch {
            try {
                val userId = currentUser.value?.uid.orEmpty()
                gameRepository.getUserGames(userId).collectLatest { _userGames.value = it }
            } catch(e: Exception) {
                _errorMessage.value = "Error getting the user games: ${e.message}"
            }
        }
    }

    fun removeSavedGame(gameID: Int) {
        viewModelScope.launch {
            gameRepository.removeSavedGame(gameID)
        }
    }

    fun getSuggestedGames(count: Int) {
        viewModelScope.launch {
            val gameIds = List(count) { (1..10000).random() }

            val games = gameIds.map { id ->
                    async {
                        try {
                            val response = ApiService.gameApi.getGameInfo(id).game.toGame()
                            response
                        } catch (e: Exception) {
                            _errorMessage.value = "Error for suggested game id $id: ${e.message}"
                            null
                        }
                    }
                }.awaitAll()

            _suggestedGames.value = games.filterNotNull().filter {
                game -> game.name.isNotEmpty()
            }

        }
    }

}