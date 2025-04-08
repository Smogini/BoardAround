package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isSuccess = userRepository.login(username, password)
                onResult(isSuccess)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Errore: ${e.message}")
            }
        }
    }

    fun registerUser(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            userRepository.registerUser(user)
        }
    }

    fun logout() = userRepository.logout()

    fun isUserLoggedIn(): Boolean = userRepository.isUserLoggedIn()

    fun retrieveUsername(): String = userRepository.retrieveUsername()

}