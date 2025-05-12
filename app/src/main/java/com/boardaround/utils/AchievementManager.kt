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
            Achievement(0, "Registrati a BoardAround!"),
            Achievement(1, "Pubblica il tuo primo post!"),
            Achievement(2, "Crea il tuo primo evento!"),
            Achievement(3, "Attiva il tema scuro!"),
            Achievement(4, "Invita un amico a un tuo evento!"),
            Achievement(5, "Aggiungi un gioco nella libreria")
        )
        _achievementList.value.forEach { achievement ->
            achievementDAO.insertAchievement(achievement)
        }
        Log.d("AchievementManager", "achievement inseriti")
//        getAllAchievements()
    }

    suspend fun unlockAchievementById(achievementId: Int) {
        try {
            achievementDAO.unlockAchievementById(achievementId)
            getAllAchievements()
        } catch (e: Exception) {
            Log.e("AchievementManager", "Errore nello sbloccare l'obiettivo: ${e.message}", e)
        }
    }

    suspend fun getAllAchievements() {
        try {
            _achievementList.value = achievementDAO.getAchievements()
            Log.d("achievementmanager", "achievement ottenuti: ${_achievementList.value}")
        } catch (e: Exception) {
            Log.e("AchievementManager", "Errore nell'ottenere gli obiettivi: ${e.message}", e)
        }
    }

}