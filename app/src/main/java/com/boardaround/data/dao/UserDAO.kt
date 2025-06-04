package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE uid = :userId")
    suspend fun deleteUser(userId: String)

    @Query("SELECT * FROM users WHERE username LIKE '%' || :username || '%'")
    suspend fun retrieveUsersByUsername(username: String): List<User>


    @Query("SELECT * FROM users WHERE uid = :userId LIMIT 1")
    suspend fun getUserByUID(userId: String): User?

}