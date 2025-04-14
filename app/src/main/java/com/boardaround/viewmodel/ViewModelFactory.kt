package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.UserRepository

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()
    private val eventDAO = database.eventDAO()
    private val postDAO = database.postDAO()

    private val userRepository = UserRepository(context, userDao)
    private val eventRepository = EventRepository(eventDAO)
    private val notificationRepository = NotificationRepository()
    private val postRepository = PostRepository(postDAO)

    fun provideAuthViewModel(): AuthViewModel = AuthViewModel(userRepository)

    fun provideUserViewModel(): UserViewModel = UserViewModel(userRepository, eventRepository, notificationRepository)

    fun providePostViewModel(): PostViewModel = PostViewModel(postRepository)

    fun provideGameViewModel(): GameViewModel = GameViewModel()

}