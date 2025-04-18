package com.boardaround.data.repositories

import com.boardaround.data.dao.GameDAO
import com.boardaround.data.entities.SavedGame

class GameRepository(private val gameDAO: GameDAO) {

    suspend fun getUserGames(username: String): List<SavedGame> =
        gameDAO.getUserGames(username)

    suspend fun addGame(newGame: SavedGame) = gameDAO.addGameToUser(newGame)

    suspend fun removeSavedGame(toRemove: SavedGame) = gameDAO.removeGameFromUser(toRemove)

}