package com.boardaround.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import com.boardaround.firebase.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationError = MutableStateFlow("")
    val registrationError: StateFlow<String> = _registrationError

    fun cleanErrorMessage() {
        _registrationError.value = ""
    }

    fun login(username: String, password: String, onSuccess: (User?) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _registrationError.value = "One of the fields is empty"
        }
        viewModelScope.launch {
            onSuccess(userRepository.login(username, password))
        }
    }

    fun setLoggedUser(user: User) {
        userRepository.setLoggedUser(user)
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            logout()
            userRepository.deleteUser(user)
        }
    }

    fun registerUser(
        username: String,
        password: String,
        name: String,
        email: String,
        dob: LocalDateTime,
        profilePic: String,
        onSuccess: () -> Unit
    ) {
        val today = LocalDateTime.now()
        val minAllowedDate = today.minusYears(12)

        if (username.isBlank() || password.isBlank() || name.isBlank()
            || email.isBlank()) {
            _registrationError.value = "Missing parameter(s)"
            return
        }
        if (password.length < 6) {
            _registrationError.value = "The password must be at least 6 characters long"
            return
        }
        if (dob.isAfter(minAllowedDate)) {
            _registrationError.value = "You must be at least 12 years old"
            return
        }

        viewModelScope.launch {
            try {
                val uid = createFirebaseUser(email, password)
                val token = FirebaseUtils.getFcmToken()

                val newUser = User(
                    username = username,
                    uid = uid,
                    name = name,
                    email = email,
                    dob = dob.toString(),
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

    suspend fun uploadProfilePicture(userId: String, imageUri: Uri): String? {
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
            val profilePicRef = storageRef.child("profile_pictures/$userId.jpg")

            val uploadTask = profilePicRef.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            _registrationError.value = "Error loading profile picture: ${e.message}"
            null
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun isUserLoggedIn(): Boolean = userRepository.isUserLoggedIn()

    private suspend fun createFirebaseUser(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid
                    if (uid != null) {
                        continuation.resume(uid)
                    } else {
                        val exceptionMsg = "FirebaseUser UID null"
                        _registrationError.value = exceptionMsg
                        continuation.resumeWithException(Exception(exceptionMsg))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

}
