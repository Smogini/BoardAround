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
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDAO()
    private val eventDAO = database.eventDAO()
    private val postDAO = database.postDAO()
    private val gameDAO = database.gameDAO()
    private val achievementDAO = database.achievementDAO()

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val sessionManager = UserSessionManager(context)

    private val userRepository = UserRepository(sessionManager, userDao, firestoreInstance)
    private val eventRepository = EventRepository(eventDAO , firestoreInstance)
    private val notificationRepository = NotificationRepository()
    private val postRepository = PostRepository(postDAO, firestoreInstance)
    private val friendshipRepository = FriendshipRepository()
    private val gameRepository = GameRepository(gameDAO)
    private val triviaRepository = TriviaRepository()
    private val newsRepository = NewsRepository()

    private val achievementManager = AchievementManager(achievementDAO)

    fun provideAuthViewModel(): AuthViewModel =
        AuthViewModel(userRepository)

    fun provideUserViewModel(): UserViewModel =
        UserViewModel(
            userRepository, notificationRepository,
            newsRepository, achievementManager
        )

    fun providePostViewModel(): PostViewModel =
        PostViewModel(postRepository, sessionManager)

    fun provideEventViewModel(): EventViewModel =
        EventViewModel(eventRepository, sessionManager)

    fun provideGameViewModel(): GameViewModel =
        GameViewModel(gameRepository, achievementManager, sessionManager)

    fun provideFriendsViewModel(): FriendsViewModel =
        FriendsViewModel(friendshipRepository)

    fun provideTriviaViewModel(): TriviaViewModel =
        TriviaViewModel(triviaRepository)

    fun provideNotificationViewModel(): NotificationViewModel =
        NotificationViewModel(notificationRepository)

    suspend fun initializeAchievementManager() {
        achievementManager.initializeAchievements()
    }

}
