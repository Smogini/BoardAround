package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.GameSearchResult
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    fun searchUsers(query: String, onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            try {
                val result = userRepository.searchUsersByUsername(query)
                onResult(result)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }

    fun searchGames(query: String, onResult: (GameSearchResult) -> Unit) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchGames(query)
                onResult(result)
                Log.d("UserViewModel", "Chiamata API completata con successo. Totale risultati: ${result.total}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }
}