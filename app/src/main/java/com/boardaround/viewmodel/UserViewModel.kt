package com.boardaround.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.entities.User
import com.boardaround.network.RetrofitInstance
import com.boardaround.utils.GameSearchResult
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val sessionManager = UserSessionManager(application)
    private val database = AppDatabase.getDatabase(application)
    private val userDAO: UserDAO = database.userDAO()

    fun insertUser(user: User, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                userDAO.insertUser(user)
                onComplete()
            } catch(e: Exception) {
                Log.e("ViewModel", "Error inserting user: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            val user = userDAO.login(username, password)
            val res = user != null
            sessionManager.setUserLoggedIn(res, username)
            onResult(res)
        }
    }

    fun logout() {
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }

    fun retrieveUsername(): String {
        return sessionManager.getUsername()
    }

    fun searchGames(query: String, onResult: (GameSearchResult) -> Unit) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchGames(query)
                onResult(result)
                Log.d("UserViewModel", "Chiamata API completata con successo. Totale risultati: ${result.total}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Errore nella chiamata API: ${e.message}", e)
            }
        }
    }
}