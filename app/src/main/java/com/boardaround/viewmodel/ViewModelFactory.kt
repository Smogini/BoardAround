package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.GameRepository

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()
    private val eventDAO = database.eventDAO()
    private val postDAO = database.postDAO()

    private val userRepository = UserRepository(context, userDao)
    private val eventRepository = EventRepository(eventDAO)
    private val notificationRepository = NotificationRepository()
    private val postRepository = PostRepository(postDAO)
    private val friendshipRepository = FriendshipRepository(userDao)
    private val gameRepository = GameRepository(userDao)

    fun provideAuthViewModel(): AuthViewModel =
        AuthViewModel(userRepository)

    fun provideUserViewModel(): UserViewModel {
        val userViewModel = UserViewModel(userRepository, notificationRepository)
        userViewModel.setFriendshipRepository(friendshipRepository)
        return userViewModel
    }

    fun providePostViewModel(): PostViewModel =
        PostViewModel(postRepository)

    fun provideEventViewModel(): EventViewModel =
        EventViewModel(eventRepository)

    fun provideGameViewModel(): GameViewModel =
        GameViewModel(gameRepository)
}
