package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boardaround.data.entities.User
import kotlinx.coroutines.flow.Flow
import com.boardaround.data.entities.Friendship

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Query("SELECT * FROM users WHERE username LIKE '%' || :username || '%'")
    suspend fun retrieveUsersByUsername(username: String): List<User>

    @Query("SELECT * FROM users WHERE username IN (SELECT friendUsername FROM friendships WHERE userUsername = :username)")
    fun getFriends(username: String): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignora se l'amicizia esiste già
    suspend fun addFriend(friendship: Friendship)

    @Query("DELETE FROM friendships WHERE userUsername = :userUsername AND friendUsername = :friendUsername")
    suspend fun removeFriend(userUsername: String, friendUsername: String)

    @Query("SELECT games FROM users WHERE username = :username")
    fun getUserGames(username: String): Flow<List<String>>

    @Query("UPDATE users SET games = :games WHERE username = :username")
    suspend fun updateUserGames(username: String, games: List<String>)
}