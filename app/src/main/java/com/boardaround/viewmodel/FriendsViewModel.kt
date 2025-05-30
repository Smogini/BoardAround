package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.User
import com.boardaround.data.repositories.FriendshipRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val repository: FriendshipRepository = FriendshipRepository()
) : ViewModel() {

    val friends: StateFlow<List<User>> = repository.friends

    val pendingFriends: StateFlow<List<Friendship>> = repository.pendingFriends

    fun sendRequest(fromUsername: String, toUsername: String) {
        viewModelScope.launch {
            repository.sendFriendRequest(fromUsername, toUsername)
            repository.loadFriendsWithStatuses(fromUsername)
        }
    }

    fun loadFriends(username: String) {
        viewModelScope.launch {
            repository.loadFriends(username)
        }
    }

    fun removeFriend(currentUserId: String, friendUserId: String) {
        viewModelScope.launch {
            Log.d("FriendsViewModel", "Removing friend between $currentUserId and $friendUserId")
            repository.removeFriend(currentUserId, friendUserId)
        }
    }

    fun acceptFriend(friendship: Friendship) {
        viewModelScope.launch {
            repository.acceptFriend(friendship)
            repository.loadFriends(friendship.toUserId ?: "")
        }
    }

    fun declineFriend(friendship: Friendship) {
        viewModelScope.launch {
            repository.declineFriend(friendship)
        }
    }


}
