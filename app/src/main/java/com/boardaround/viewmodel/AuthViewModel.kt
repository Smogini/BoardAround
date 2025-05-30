package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import com.boardaround.firebase.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationError = MutableStateFlow("")
    val registrationError: StateFlow<String> = _registrationError

    fun cleanErrorMessage() {
        _registrationError.value = ""
    }

    fun login(email: String, password: String, onSuccess: (User?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _registrationError.value = "One of the fields is empty"
        }
        viewModelScope.launch {
            onSuccess(userRepository.login(email, password))
        }
    }

    fun setLoggedUser(user: User) {
        userRepository.setLoggedUser(user)
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            logout()
            userRepository.deleteUser()
        }
    }

    fun registerUser(
        username: String,
        password: String,
        name: String,
        email: String,
        dob: String,
        profilePic: String,
        onSuccess: () -> Unit
    ) {
        val formattedDob = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val minAllowedDate = LocalDateTime.now().minusYears(12).toLocalDate()

        if (username.isBlank() || password.isBlank() || name.isBlank()
            || email.isBlank()) {
            _registrationError.value = "Missing parameter(s)"
            return
        }
        if (password.length < 6) {
            _registrationError.value = "The password must be at least 6 characters long"
            return
        }
        if (formattedDob.isAfter(minAllowedDate)) {
            _registrationError.value = "You must be at least 12 years old"
            return
        }

        viewModelScope.launch {
            try {
                val uid = userRepository.createFirebaseUser(email, password)
                val token = FirebaseUtils.getFcmToken()

                val newUser = User(
                    username = username,
                    uid = uid,
                    name = name,
                    email = email,
                    dob = dob,
                    profilePic = profilePic,
                    fcmToken = token
                )

                FirebaseUtils.registerUser(newUser)
                userRepository.registerUser(newUser)
                onSuccess()
            } catch (e: Exception) {
                _registrationError.value = "Error during user registration: ${e.message}"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }


    fun isUserLoggedIn(): Boolean = userRepository.isUserLoggedIn()

}
