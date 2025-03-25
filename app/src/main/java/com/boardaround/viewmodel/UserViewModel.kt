package com.boardaround.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.entities.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val userDAO: UserDAO
    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    init {
        val db = AppDatabase.getDatabase(application)
        userDAO = db.userDAO()
    }

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

    fun login(username: String, password: String): User?{
        var res: User? = null
        viewModelScope.launch {
            val user = userDAO.getUser(username, password)
            Log.e("ViewModel", "user: $user")
            res = user
        }
        return res
    }
}