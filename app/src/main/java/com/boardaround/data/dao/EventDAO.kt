package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boardaround.data.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {

    @Insert
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event WHERE name LIKE '%' || :eventName || '%'")
    suspend fun searchEventsByName(eventName: String): List<Event>

    @Query("SELECT * FROM event ORDER BY name DESC")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM event WHERE author = :username")
    fun getEventsByUsername(username: String): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE address LIKE '%' || :address || '%'")
    suspend fun searchEventsByAddress(address: String): List<Event>

}