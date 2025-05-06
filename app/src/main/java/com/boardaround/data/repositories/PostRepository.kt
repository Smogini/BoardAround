package com.boardaround.data.repositories

import com.boardaround.data.dao.PostDao
import com.boardaround.data.entities.Post

class PostRepository(private val postDao: PostDao) {
    suspend fun insertPost(post: Post) = postDao.insertPost(post)

    suspend fun getPostsByUsername(username: String): List<Post> =
        postDao.getPostsByUsername(username)

}
