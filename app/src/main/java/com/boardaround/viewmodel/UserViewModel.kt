package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
): ViewModel() {

    private val _hasNewNotifications = MutableStateFlow(false)
    val hasNewNotifications: StateFlow<Boolean> = _hasNewNotifications

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    private val _usersFound = MutableStateFlow<List<User>>(emptyList())
    val usersFound: StateFlow<List<User>> = _usersFound

    fun selectUser(user: User) {
        this._selectedUser.value = user
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

    fun refreshNotificationStatus() {
        viewModelScope.launch {
            val newNotifications = notificationRepository.hasUnread()
            _hasNewNotifications.value = newNotifications
        }
    }
}