package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Article
import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.FriendshipRepository
import com.boardaround.data.repositories.NewsRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.utils.AchievementManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
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

    val currentUser = userRepository.currentUser

    private val _userFriends = MutableStateFlow<List<User>>(emptyList())
    val userFriends: StateFlow<List<User>> = _userFriends

    val pendingFriendships = MutableStateFlow<List<Friendship>>(emptyList())

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        fetchBoardGameNews()
        initializeAchievements()
    }

    /* TODO: fix the user id (is currently null) */
    private fun initializeAchievements() {
        viewModelScope.launch {
            if (!achievementManager.isAlreadyInitialized()) {
                achievementManager.initializeAchievements(currentUser.value?.uid.orEmpty())
            }
        }
    }

    fun getAllAchievements() {
        viewModelScope.launch {
            achievementManager.getAllAchievements()
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun selectUser(user: User) {
        this._selectedUser.value = user
    }

    fun loadFriends() {
        viewModelScope.launch {
            try {
                val currentUserUID = currentUser.value?.uid.orEmpty()
                val friendUsernames = friendshipRepository.getAcceptedFriendUsernames(currentUserUID)
                val users = friendshipRepository.getUsersByUsernames(friendUsernames)
                _userFriends.value = users

                val pending = friendshipRepository.getPendingFriendships(currentUserUID)
                pendingFriendships.value = pending
            } catch (e: Exception) {
                _errorMessage.value = "Error loading friends: ${e.message}"
                _userFriends.value = emptyList()
                pendingFriendships.value = emptyList()
            }
        }
    }

    fun sendFriendshipRequest(fromUserUID: String, toUserUID: String) {
        viewModelScope.launch {
            friendshipRepository.sendFriendRequest(fromUserUID, toUserUID)
        }
    }

    fun acceptFriendRequest(friendship: Friendship) {
        viewModelScope.launch {
            friendshipRepository.acceptFriend(friendship)
            loadFriends()
        }
    }

    fun declineFriendRequest(friendship: Friendship) {
        viewModelScope.launch {
            friendshipRepository.declineFriend(friendship)
            loadFriends()
        }
    }

    fun removeFriend(friendUserId: String) {
        val currUserUID = currentUser.value?.username
        if (currUserUID.isNullOrEmpty()){
            _errorMessage.value = "Error deleting friend: the current user uid is null or empty"
            return
        }

        viewModelScope.launch {
            friendshipRepository.removeFriend(currUserUID, friendUserId)
            loadFriends()
        }
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _usersFound.value = userRepository.searchUsersByUsername(query)
            } catch (e: Exception) {
                _errorMessage.value = "User search error: ${e.message}"
            }
        }
    }

    fun fetchBoardGameNews(language: String = "en") {
        viewModelScope.launch {
            _articleList.value = newsRepository.getBoardGameNews(language = language)
        }
    }

}
