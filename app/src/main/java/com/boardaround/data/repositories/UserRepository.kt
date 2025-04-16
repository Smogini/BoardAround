package com.boardaround.data.repositories

import android.content.Context
import android.util.Log
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User
import kotlinx.coroutines.flow.Flow
import kotlin.collections.remove
import kotlin.collections.toMutableList

class UserRepository(
    context: Context,
    private val userDao: UserDAO
) {

    private val sessionManager = UserSessionManager(context)

    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.retrieveUser(username)
        val res = user?.password.equals(password)
        sessionManager.setUserLoggedIn(res, username)
        return res
    }

    fun logout() {
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }

    suspend fun getUserData(username: String): User? {
        return userDao.retrieveUser(username)
    }

    fun getUserGames(username: String): Flow<List<String>> {
        return userDao.getUserGames(username)
    }

    suspend fun removeGame(username: String, game: String) {
        Log.d("UserRepository", "removeGame called for user: $username, game: $game")
        val user = userDao.getUser(username)
        if (user != null) {
            val updatedGames = user.games.toMutableList()
            updatedGames.remove(game)
            Log.d("UserRepository", "Updated games list: $updatedGames")
            userDao.updateUserGames(username, updatedGames)
        } else {
            Log.e("UserRepository", "User not found: $username")
        }
    }

    fun retrieveUsername():String {
        return sessionManager.getUsername()
    }

    suspend fun searchUsersByUsername(username: String): List<User> {
        val users = userDao.retrieveUsersByUsername(username)
        return users
    }
}