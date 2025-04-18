package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Game
import com.boardaround.data.entities.GameSearchResult
import com.boardaround.data.entities.SavedGame
import com.boardaround.data.repositories.GameRepository
import com.boardaround.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val gameRepository: GameRepository): ViewModel() {

    private var _gamesFound = MutableStateFlow(GameSearchResult(0, emptyList()))
    val gamesFound: StateFlow<GameSearchResult> = _gamesFound

    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame: StateFlow<Game?> = _selectedGame

    private val _userGames = MutableStateFlow<List<SavedGame>>(emptyList())
    val userGames: StateFlow<List<SavedGame>> = _userGames

    fun selectGame(game: Game) {
        _selectedGame.value = game
    }

    fun searchGames(query: String) {
        viewModelScope.launch {
            try {
                _gamesFound.value = RetrofitInstance.api.searchGames(query)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }

    fun addGame(newGame: SavedGame) {
        viewModelScope.launch {
            gameRepository.addGame(newGame)
        }
    }

    fun getUserGames(username: String) {
        viewModelScope.launch {
            try {
                _userGames.value= gameRepository.getUserGames(username)
            } catch(e: Exception) {
                Log.e("UserViewModel", "Errore nell'ottenere i giochi: ${e.message}", e)
            }
        }
    }

    fun removeSavedGame(toRemove: SavedGame) {
        viewModelScope.launch {
            gameRepository.removeSavedGame(toRemove)
            getUserGames(toRemove.user)
        }
    }

}