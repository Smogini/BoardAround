package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.GameRepository
import com.boardaround.data.repositories.TriviaRepository
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()
    private val eventDAO = database.eventDAO()
    private val postDAO = database.postDAO()
    private val gameDAO = database.gameDAO()

    private val firestoreInstance = FirebaseFirestore.getInstance()

    private val userRepository = UserRepository(context, userDao, firestoreInstance)
    private val eventRepository = EventRepository(eventDAO , firestoreInstance)
    private val notificationRepository = NotificationRepository()
    private val postRepository = PostRepository(postDAO, firestoreInstance)
    private val friendshipRepository = FriendshipRepository(userDao)
    private val gameRepository = GameRepository(gameDAO)
    private val triviaRepository = TriviaRepository()

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

    fun provideTriviaViewModel(): TriviaViewModel =
        TriviaViewModel(triviaRepository)
}
