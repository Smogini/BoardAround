package com.boardaround.data.repositories

import android.content.Context
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User

class UserRepository(
    context: Context,
    private val userDAO: UserDAO
) {

    private val sessionManager = UserSessionManager(context)

    suspend fun registerUser(user: User) {
        userDAO.insertUser(user)
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = userDAO.retrieveUser(username)
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
        return userDAO.retrieveUser(username)
    }

    fun retrieveUsername():String {
        return sessionManager.getUsername()
    }

    suspend fun searchUsersByUsername(username: String): List<User> {
        val users = userDAO.retrieveUsersByUsername(username)
        return users
    }
}