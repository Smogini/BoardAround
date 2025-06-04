package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.SavedGame
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGameToUser(newGame: SavedGame)

    @Query("DELETE FROM saved_game WHERE gameId = :gameId")
    suspend fun removeGameFromUser(gameId: Int)

    @Query("SELECT * FROM saved_game WHERE userId = :userId")
    fun getUserGames(userId: String): Flow<List<SavedGame>>

}