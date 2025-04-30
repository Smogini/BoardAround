package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.SavedGame

@Dao
interface GameDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGameToUser(newGame: SavedGame)

    @Delete
    suspend fun removeGameFromUser(toRemove: SavedGame)

    @Query("SELECT * FROM saved_game WHERE user = :username")
    suspend fun getUserGames(username: String): List<SavedGame>

}