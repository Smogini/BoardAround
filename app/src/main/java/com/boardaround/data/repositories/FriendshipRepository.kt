package com.boardaround.data.repositories

import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.User
import kotlinx.coroutines.flow.Flow

class FriendshipRepository(private val userDao: UserDAO) {

    fun getFriends(username: String): Flow<List<User>> =
        userDao.getFriends(username)

    suspend fun addFriend(userUsername: String, friendUsername: String) {
        userDao.addFriend(Friendship(userUsername, friendUsername))
    }

    suspend fun removeFriend(userUsername: String, friendUsername: String) {
        userDao.removeFriend(userUsername, friendUsername)
    }
}