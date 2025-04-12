package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.GameSearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val notificationRepository: NotificationRepository
): ViewModel() {

    private val _hasNewNotifications = MutableStateFlow(false)
    val hasNewNotifications: StateFlow<Boolean> = _hasNewNotifications

    fun searchUsers(query: String, onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            try {
                val usersFound = userRepository.searchUsersByUsername(query)
                onResult(usersFound)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella ricerca utente: ${e.message}", e)
            }
        }
    }

    fun getUserData(username: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData(username) // Ottieni i dati dell'utente
                onResult(user) // Passa i dati alla UI
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nel recupero dell'utente: ${e.message}", e)
                onResult(null) // In caso di errore, restituisci null
            }
        }
    }

    fun searchGames(query: String, onResult: (GameSearchResult) -> Unit) {
        viewModelScope.launch {
            try {
                val gamesFound = RetrofitInstance.api.searchGames(query)
                onResult(gamesFound)
                Log.d("UserViewModel", "Chiamata API completata con successo. Totale risultati: ${gamesFound.total}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }

    fun searchEvent(query: String, onResult: (List<Event>) -> Unit) {
        viewModelScope.launch {
            try {
                val eventFound = eventRepository.searchEvent(query)
                onResult(eventFound)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella ricerca dell'evento: ${e.message}", e)
            }
        }
    }

    fun createNewEvent(newEvent: Event) {
        viewModelScope.launch {
            try {
                eventRepository.newEvent(newEvent)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nell'inserimento dell'evento': ${e.message}", e)
            }
        }
    }

    fun refreshNotificationStatus() {
        viewModelScope.launch {
            val newNotifications = notificationRepository.hasUnread()
            _hasNewNotifications.value = newNotifications
        }
    }
}