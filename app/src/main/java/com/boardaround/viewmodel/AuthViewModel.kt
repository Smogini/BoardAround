package com.boardaround.viewmodel

import android.net.Uri
import android.util.Log
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError

    fun login(username: String, password: String, onSuccess: (User?) -> Unit) {
        viewModelScope.launch {
            onSuccess(userRepository.login(username, password))
        }
    }

    fun setLoggedUser(user: User) {
        userRepository.setLoggedUser(user)
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            logout()
        }
    }

    fun registerUser(
        username: String,
        password: String,
        name: String,
        email: String,
        dob: String,
        profilePic: String
    ) {
        viewModelScope.launch {
            try {
                val uid = createFirebaseUser(email, password)
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

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Errore durante la registrazione: ${e.message}", e)
                _registrationError.value = "Errore durante la registrazione: ${e.message}"
            }
        }
    }

    suspend fun uploadProfilePicture(userId: String, imageUri: Uri): String? {
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
            val profilePicRef = storageRef.child("profile_pictures/$userId.jpg") // Usa l'UID come nome del file

            val uploadTask = profilePicRef.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Errore nel caricare l'immagine del profilo", e)
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
                        continuation.resumeWithException(Exception("FirebaseUser UID null"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

}
