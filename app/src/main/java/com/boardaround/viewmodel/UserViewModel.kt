package com.boardaround.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.entities.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val userDAO: UserDAO

    init {
        val db = AppDatabase.getDatabase(application)
        userDAO = db.userDAO()
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            userDAO.insertUser(user)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDAO.getUser(username, password)
        }
    }
}