package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.AchievementType
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import com.boardaround.firebase.FirebaseUtils
import com.boardaround.utils.AchievementManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AuthViewModel(
    private val userRepository: UserRepository,
    private val achievementManager: AchievementManager
) : ViewModel() {

    private val _registrationError = MutableStateFlow("")
    val registrationError: StateFlow<String> = _registrationError

    fun cleanErrorMessage() {
        _registrationError.value = ""
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _registrationError.value = "One of the fields is empty"
            return
        }
        viewModelScope.launch {
            try {
                userRepository.login(email, password)
                achievementManager.unlockAchievement(AchievementType.FirstLogin)
                onSuccess()
            } catch (e: Exception) {
                _registrationError.value = e.message.orEmpty()
            }
        }
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
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
                val firebaseUser = userRepository.createFirebaseUser(email, password)
                val token = FirebaseUtils.getFcmToken()

                val newUser = User(
                    username = username,
                    uid = firebaseUser.uid,
                    name = name,
                    email = email,
                    dob = dob,
                    profilePic = profilePic,
                    fcmToken = token
                )

                FirebaseUtils.registerUser(newUser)
                userRepository.registerUser(newUser)
                achievementManager.unlockAchievement(AchievementType.Register)
                onSuccess()
            } catch (e: Exception) {
                _registrationError.value = e.message.orEmpty()
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
