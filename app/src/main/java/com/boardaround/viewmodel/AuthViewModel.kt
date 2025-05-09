package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.boardaround.firebase.FirebaseUtils
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

@Suppress("CAST_NEVER_SUCCEEDS")
class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true // Inizia il caricamento
            _registrationError.value = null // Resetta l'errore
            try {
                val isSuccess = userRepository.login(username, password)
                _loginSuccess.value = isSuccess
                if (!isSuccess) {
                    _registrationError.value = "Credenziali non valide o utente non trovato."
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Errore login: ${e.message}", e)
                _registrationError.value = "Errore durante il login: ${e.message}"
            } finally {
                _isLoading.value = false // Termina il caricamento
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            logout()
        }
    }

    fun registerUser(user: User, onSuccess: () -> Unit) {
        try {
            FirebaseUtils.registerUser(user) // Deve essere NON sospesa!
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }





    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            // Potresti voler aggiornare uno stato per indicare che l'utente è disconnesso
        }
    }

    fun isUserLoggedIn(): Boolean = userRepository.isUserLoggedIn()

    fun clearRegistrationError() {
        _registrationError.value = null
    }

    // All'interno di AuthViewModel

    fun createFirebaseUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Crea l'utente con email e password usando Firebase Authentication
                val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // Chiama il callback onSuccess con l'UID dell'utente
                    onSuccess(firebaseUser.uid)
                } else {
                    throw Exception("Errore nella creazione dell'utente. FirebaseUser è null.")
                }
            } catch (e: Exception) {
                // Gestisci eventuali errori e chiama onFailure
                Log.e("AuthViewModel", "Errore nella creazione dell'utente: ${e.message}", e)
                onFailure(e)
            }
        }
    }

    fun saveUserLocally(user: User) {
        viewModelScope.launch {
            try {
                userRepository.saveUser(user)
            } catch (e: Exception) {
                // Gestisci eventuali errori qui
                e.printStackTrace()
            }
        }
    }


}

