package com.boardaround.data.repositories


import com.boardaround.data.dao.UserDAO
import kotlinx.coroutines.flow.Flow

class GameRepository(private val userDao: UserDAO) {
    fun getUserGames(username: String): Flow<List<String>> {
        return userDao.getUserGames(username)
    }

    suspend fun addGame(username: String, game: String) {
        val currentGames = userDao.getUser(username)?.games ?: emptyList()
        val updatedGames = currentGames + game
        userDao.updateUserGames(username, updatedGames)
    }

    suspend fun removeGame(username: String, game: String) {
        val currentGames = userDao.getUser(username)?.games ?: emptyList()
        val updatedGames = currentGames.filter { it != game }
        userDao.updateUserGames(username, updatedGames)
    }
}