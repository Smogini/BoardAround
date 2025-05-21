package com.boardaround.viewmodel

import android.content.Context
import com.boardaround.data.UserSessionManager
import com.boardaround.data.database.AppDatabase
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.GameRepository
import com.boardaround.data.repositories.NewsRepository
import com.boardaround.data.repositories.TriviaRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.boardaround.utils.AchievementManager
import com.boardaround.viewmodel.NewsViewModel
import com.boardaround.network.NewsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
    private val friendshipRepository = FriendshipRepository(userDao)
    private val gameRepository = GameRepository(gameDAO)
    private val triviaRepository = TriviaRepository()

    private val achievementManager = AchievementManager(achievementDAO)

    private val newsApiBaseUrl = "https://newsapi.org/"
    private val NEWS_API_KEY = "0eaaf73276024761bf5c7b2a63083ca6"
    private val newsLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // O Level.BASIC, Level.HEADERS
    }

    private val newsOkHttpClient: OkHttpClient by lazy { // Usa by lazy se vuoi inizializzazione differita
        OkHttpClient.Builder()
            .addInterceptor(newsLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val newsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(newsApiBaseUrl)
            .client(newsOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val newsApiService: NewsApiService by lazy {
        newsRetrofit.create(NewsApiService::class.java)
    }

    private val newsRepository: NewsRepository by lazy {
        NewsRepository(newsApiService, NEWS_API_KEY) // Passa la apiKey qui
    }

    fun provideAuthViewModel(): AuthViewModel =
        AuthViewModel(userRepository)

    fun provideUserViewModel(): UserViewModel {
        val userViewModel = UserViewModel(userRepository, notificationRepository, friendshipRepository, achievementManager)
//        userViewModel.setFriendshipRepository(friendshipRepository)
        return userViewModel
    }

    fun providePostViewModel(): PostViewModel =
        PostViewModel(postRepository, sessionManager)

    fun provideEventViewModel(): EventViewModel =
        EventViewModel(eventRepository, sessionManager)

    fun provideGameViewModel(): GameViewModel =
        GameViewModel(gameRepository, achievementManager, sessionManager)

    fun provideTriviaViewModel(): TriviaViewModel =
        TriviaViewModel(triviaRepository)

    fun provideNewsViewModel(): NewsViewModel =
        NewsViewModel(newsRepository)

    suspend fun initializeAchievementManager() {
        achievementManager.initializeAchievements()
    }

}
