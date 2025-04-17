package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val isSuccess = userRepository.login(username, password)
                _loginSuccess.value = isSuccess
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Errore: ${e.message}")
            }
        }
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            userRepository.registerUser(user)
        }
    }

    fun logout() = userRepository.logout()

    fun isUserLoggedIn(): Boolean = userRepository.isUserLoggedIn()

}