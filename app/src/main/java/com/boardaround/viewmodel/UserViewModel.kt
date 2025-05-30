package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Article
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.NewsRepository
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.utils.AchievementManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val newsRepository: NewsRepository,
    private val achievementManager: AchievementManager
): ViewModel() {

    val achievementList = achievementManager.achievementList

    private val _articleList = MutableStateFlow<List<Article>>(emptyList())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _hasNewNotifications = MutableStateFlow(false)
    val hasNewNotifications: StateFlow<Boolean> = _hasNewNotifications

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    private val _usersFound = MutableStateFlow<List<User>>(emptyList())
    val usersFound: StateFlow<List<User>> = _usersFound

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser



    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount: StateFlow<Int> = _unreadNotificationCount

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun selectUser(user: User) {
        this._selectedUser.value = user
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                _usersFound.value = userRepository.searchUsersByUsername(query)
            } catch (e: Exception) {
                _errorMessage.value = "User search error: ${e.message}"
            }
        }
    }

    fun getAllAchievements() {
        viewModelScope.launch {
            achievementManager.getAllAchievements()
        }
    }

    fun setUnreadNotificationCount(count: Int) {
        _unreadNotificationCount.value = count
    }

    fun getUserId(): String {
        return _currentUser.value?.uid ?: ""
    }

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val user = userRepository.getCurrentUser()
        _currentUser.value = user
    }


    fun getUsername(): String =
        userRepository.getCurrentUser()?.username ?: "No username"

    fun getCurrentUID(): String? =
        userRepository.getCurrentUID()

    fun fetchBoardGameNews(language: String = "en") {
        viewModelScope.launch {
            _articleList.value = newsRepository.getBoardGameNews(language = language)
        }
    }

//    fun refreshNotificationStatus() {
//        viewModelScope.launch {
//            val newNotifications = notificationRepository.hasUnread()
//            _hasNewNotifications.value = newNotifications
//        }
//    }

}