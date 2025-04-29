package com.boardaround.data.repositories

import android.content.Context
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User

class UserRepository(
    context: Context,
    private val userDao: UserDAO
) {

    private val sessionManager = UserSessionManager(context)

    suspend fun deleteUser(toDelete: User) {
        userDao.deleteUser(toDelete)
    }


    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUser(username)
        val res = user?.password == password
        user?.let { sessionManager.setUserLoggedIn(it, res) }
        return res
    }

    fun logout() {
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }

    suspend fun searchUsersByUsername(username: String): List<User> {
        val users = userDao.retrieveUsersByUsername(username)
        return users
    }

}