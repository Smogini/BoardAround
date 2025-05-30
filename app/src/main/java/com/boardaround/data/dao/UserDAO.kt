package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE uid = :userUID")
    suspend fun deleteUser(userUID: String)

    @Query("SELECT * FROM users WHERE username LIKE '%' || :username || '%'")
    suspend fun retrieveUsersByUsername(username: String): List<User>


    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

}