package com.boardaround.utils

import android.util.Log
import com.boardaround.data.dao.AchievementDAO
import com.boardaround.data.entities.Achievement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AchievementManager(private val achievementDAO: AchievementDAO) {

    private val _achievementList = MutableStateFlow<List<Achievement>>(emptyList())
    val achievementList: StateFlow<List<Achievement>> = _achievementList

    suspend fun initializeAchievements() {
        _achievementList.value = listOf(
            Achievement(0, "Register on BoardAround!"),
            Achievement(1, "Publish your first post!"),
            Achievement(2, "Create your first event!"),
            Achievement(3, "Activate the dark theme!"),
            Achievement(4, "Invite a friend to your event!"),
            Achievement(5, "Add a game to the library")
        )
        _achievementList.value.forEach { achievement ->
            achievementDAO.insertAchievement(achievement)
        }
//        getAllAchievements()
    }

    suspend fun unlockAchievementById(achievementId: Int) {
        try {
            achievementDAO.unlockAchievementById(achievementId)
            getAllAchievements()
        } catch (e: Exception) {
            Log.e("AchievementManager", "Error unlocking the achievement: ${e.message}", e)
        }
    }

    suspend fun getAllAchievements() {
        try {
            _achievementList.value = achievementDAO.getAchievements()
        } catch (e: Exception) {
            Log.e("AchievementManager", "Error getting achievements: ${e.message}", e)
        }
    }

}