package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.Achievement

@Dao
interface AchievementDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(newAchievement: Achievement)

    @Delete
    suspend fun deleteAchievement(toDelete: Achievement)

    @Query("UPDATE achievement SET id = id, description = description, isUnlocked = true WHERE id = :achievementId")
    suspend fun unlockAchievementById(achievementId: String)

    @Query("SELECT * FROM achievement")
    suspend fun getAchievements(): List<Achievement>

    @Query("SELECT COUNT(*) FROM achievement")
    suspend fun checkEntriesCount(): Int
}