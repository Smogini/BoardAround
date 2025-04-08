package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.UserRepository

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()

    private val userRepository = UserRepository(context, userDao)

    fun provideAuthViewModel(): AuthViewModel = AuthViewModel(userRepository)

    fun provideUserViewModel(): UserViewModel = UserViewModel(userRepository)
}