package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.boardaround.data.entities.Friendship
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.GameRepository

class UserViewModel(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val gameRepository: GameRepository
): ViewModel() {

    private lateinit var friendshipRepository: FriendshipRepository

    private val _hasNewNotifications = MutableStateFlow(false)
    val hasNewNotifications: StateFlow<Boolean> = _hasNewNotifications

    private val _objectives = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val objectives: StateFlow<Map<String, Boolean>> = _objectives

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    private val _usersFound = MutableStateFlow<List<User>>(emptyList())
    val usersFound: StateFlow<List<User>> = _usersFound

    init {
        _objectives.value =
            mutableListOf(
                "Registrati a BoardAround!",
                "Pubblica il tuo primo post!",
                "Crea il tuo primo evento!",
                "Attiva il tema scuro!",
                "Invita un amico a un tuo evento!",
                "Aggiungi un gioco nella libreria"
            ).associateWith { false }.toMutableMap()
    }

    fun selectUser(user: User) {
        this._selectedUser.value = user
    }

    fun setFriendshipRepository(repository: FriendshipRepository) {
        this.friendshipRepository = repository
    }

    fun getFriends(username: String): Flow<List<User>> {
        return friendshipRepository.getFriends(username)
    }

    fun addFriend(userUsername: String, friendUsername: String) {
        viewModelScope.launch {
            friendshipRepository.addFriend(userUsername, friendUsername)
        }
    }

    fun removeFriend(userUsername: String, friendUsername: String) {
        viewModelScope.launch {
            friendshipRepository.removeFriend(userUsername, friendUsername)
        }
    }


    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                _usersFound.value = userRepository.searchUsersByUsername(query)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella ricerca utente: ${e.message}", e)
            }
        }
    }

    fun getUserData(username: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData(username)
                onResult(user)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nel recupero dell'utente: ${e.message}", e)
            }
        }
    }

    fun unlockObjective(objective: String) {
        val updatedObjects = _objectives.value.toMutableMap()
        updatedObjects[objective] = true
        _objectives.value = updatedObjects
    }

    fun addGame(username: String, game: String) {
        viewModelScope.launch {
            gameRepository.addGame(username, game)
        }
    }

    fun getUserGames(username: String): Flow<List<String>> {
        return userRepository.getUserGames(username)
    }


    fun removeGame(username: String, game: String) {
        viewModelScope.launch {
            gameRepository.removeGame(username, game)
        }
    }

//    fun refreshNotificationStatus() {
//        viewModelScope.launch {
//            val newNotifications = notificationRepository.hasUnread()
//            _hasNewNotifications.value = newNotifications
//        }
//    }
}