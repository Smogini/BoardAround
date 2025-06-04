package com.boardaround.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boardaround.data.entities.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM posts ORDER BY id DESC")
    suspend fun getAllPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE author = :username")
    fun getPostsByUsername(username: String): Flow<List<Post>>

}