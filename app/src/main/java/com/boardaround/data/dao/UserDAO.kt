package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun retrieveUser(username: String): User?

    @Query("SELECT * FROM users WHERE username LIKE '%' || :username || '%'")
    suspend fun retrieveUsersByUsername(username: String): List<User>
}