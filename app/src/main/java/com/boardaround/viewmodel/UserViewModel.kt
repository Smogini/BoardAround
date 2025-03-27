package com.boardaround.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

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

    fun login(username: String, password: String): StateFlow<User?>{
        val res = MutableStateFlow<User?>(null)
        viewModelScope.launch {
            val user = userDAO.login(username, password)
            res.value = user
            Log.d("ViewModel", "user: $res")
        }
        return res
    }
}