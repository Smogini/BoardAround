package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.UserSessionManager
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.GameRepository
import com.boardaround.data.repositories.NewsRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.TriviaRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.utils.AchievementManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()
    private val eventDAO = database.eventDAO()
    private val postDAO = database.postDAO()
    private val gameDAO = database.gameDAO()
    private val achievementDAO = database.achievementDAO()

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val sessionManager = UserSessionManager(context)

    private val achievementManager = AchievementManager(achievementDAO)

    private val userRepository =
        UserRepository(sessionManager, userDao, firestoreDB, firebaseAuth)
    private val eventRepository = EventRepository(eventDAO , firestoreDB)
    private val notificationRepository = NotificationRepository(firestoreDB)
    private val postRepository = PostRepository(postDAO, firestoreDB)
    private val friendshipRepository = FriendshipRepository(firestoreDB)
    private val gameRepository = GameRepository(gameDAO, achievementManager)
    private val triviaRepository = TriviaRepository()
    private val newsRepository = NewsRepository()

    fun provideAuthViewModel(): AuthViewModel =
        AuthViewModel(userRepository, achievementManager)

    fun provideUserViewModel(): UserViewModel =
        UserViewModel(
            userRepository, friendshipRepository,
            newsRepository, achievementManager
        )

    fun providePostViewModel(): PostViewModel =
        PostViewModel(postRepository, userRepository)

    fun provideEventViewModel(): EventViewModel =
        EventViewModel(eventRepository, userRepository, achievementManager)

    fun provideGameViewModel(): GameViewModel =
        GameViewModel(gameRepository, userRepository)

    fun provideTriviaViewModel(): TriviaViewModel =
        TriviaViewModel(triviaRepository)

    fun provideNotificationViewModel(): NotificationViewModel =
        NotificationViewModel(notificationRepository, firestoreDB)

}
