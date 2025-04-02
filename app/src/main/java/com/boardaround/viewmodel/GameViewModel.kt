package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games

    init {
        Log.d("GameViewModel", "ViewModel creato")
        fetchGames()
    }

    fun fetchGames(query: String = "Dixit") {
        Log.d("GameViewModel", "fetchGames() chiamata con query: $query")
        viewModelScope.launch {
            Log.d("GameViewModel", "Coroutine avviata")
            try {
                Log.d("GameViewModel", "Chiamata API in corso...")
                val result = RetrofitInstance.api.searchGames(query)
                val gameList = result.games
                val totalGames = result.total
                Log.d("GameViewModel",  "Chiamata API completata con successo. Numero di giochi :${gameList.size}, Totale risultati: $totalGames}")
                Log.d("GameViewModel", "Chiamata API completata con successo. Numero di giochi: ${gameList.size}")
                _games.value = gameList
                Log.d("GameViewModel", "_games aggiornato con ${gameList.size} giochi")
            } catch (e: Exception) {
                Log.e("GameViewModel", "Errore nella chiamata API: ${e.message}", e)
            } finally {
                Log.d("GameViewModel", "Coroutine completata (successo o fallimento)")
            }
        }
    }
}