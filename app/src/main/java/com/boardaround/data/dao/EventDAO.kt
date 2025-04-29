package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boardaround.data.entities.Event

@Dao
interface EventDAO {

    @Insert
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM events WHERE name LIKE '%' || :eventName || '%'")
    suspend fun searchEventsByName(eventName: String): List<Event>

    @Query("SELECT * FROM events ORDER BY name DESC")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE author = :username")
    suspend fun getEventsByUsername(username: String): List<Event>

}