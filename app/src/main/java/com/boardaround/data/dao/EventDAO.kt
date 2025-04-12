package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.Event

@Dao
interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM events WHERE name LIKE '%' || :eventName || '%'")
    suspend fun searchEvents(eventName: String): List<Event>
}