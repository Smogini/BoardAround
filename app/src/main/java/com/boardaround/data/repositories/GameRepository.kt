package com.boardaround.data.repositories

import com.boardaround.data.dao.GameDAO
import com.boardaround.data.entities.AchievementType
import com.boardaround.data.entities.SavedGame
import com.boardaround.utils.AchievementManager
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDAO: GameDAO, private val achievementManager: AchievementManager) {

    fun getUserGames(userId: String): Flow<List<SavedGame>> =
        gameDAO.getUserGames(userId)

    suspend fun addGame(newGame: SavedGame) {
        gameDAO.addGameToUser(newGame)
        achievementManager.unlockAchievement(AchievementType.AddGame)
    }

    suspend fun removeSavedGame(gameId: Int) = gameDAO.removeGameFromUser(gameId)

}
