package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.repositories.GameRepository
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.Game
import com.boardaround.utils.GameSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val gameRepository: GameRepository): ViewModel() {

    private var _gamesFound = MutableStateFlow(GameSearchResult(0, emptyList()))
    val gamesFound: StateFlow<GameSearchResult> = _gamesFound

    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame: StateFlow<Game?> = _selectedGame

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

    fun addGame(username: String, game: String) {
        viewModelScope.launch {
            gameRepository.addGame(username, game)
        }
    }

    fun getUserGames(username: String): Flow<List<String>> {
        return gameRepository.getUserGames(username)
    }

    fun removeGame(username: String, game: String) {
        viewModelScope.launch {
            gameRepository.removeGame(username, game)
        }
    }
}