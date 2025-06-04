package com.boardaround.utils

import com.boardaround.data.dao.AchievementDAO
import com.boardaround.data.entities.Achievement
import com.boardaround.data.entities.AchievementType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AchievementManager(
    private val achievementDAO: AchievementDAO
) {

    private val _achievementList = MutableStateFlow<List<Achievement>>(emptyList())
    val achievementList: StateFlow<List<Achievement>> = _achievementList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    suspend fun isAlreadyInitialized(): Boolean =
        achievementDAO.checkEntriesCount() > 0

    suspend fun initializeAchievements(userId: String) {
        val achievementTypeList = listOf(
            AchievementType.Register,
            AchievementType.FirstLogin,
            AchievementType.FirstPost,
            AchievementType.FirstEvent,
            AchievementType.DarkTheme,
            AchievementType.InviteToEvent,
            AchievementType.AddGame
        )
        achievementTypeList.forEach { achievement ->
            val toInsert = Achievement(
                id = achievement.id,
                description = achievement.description,
                isUnlocked = false,
                userId = userId
            )
            achievementDAO.insertAchievement(toInsert)
        }
        getAllAchievements()
    }

    suspend fun unlockAchievement(achievement: AchievementType) {
        try {
            achievementDAO.unlockAchievementById(achievement.id)
            getAllAchievements()
        } catch (e: Exception) {
            _errorMessage.value = "Error unlocking the achievement: ${e.message}"
        }
    }

    suspend fun getAllAchievements() {
        try {
            _achievementList.value = achievementDAO.getAchievements()
        } catch (e: Exception) {
            _errorMessage.value = "Error getting achievements: ${e.message}"
        }
    }

}