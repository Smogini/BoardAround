package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.Game
import com.boardaround.utils.GameSearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

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
                Log.d("UserViewModel", "Chiamata API completata con successo. Totale risultati: ${_gamesFound.value.total}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }
}