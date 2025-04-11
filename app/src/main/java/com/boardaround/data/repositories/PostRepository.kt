package com.boardaround.data.repositories

import com.boardaround.data.dao.PostDao
import com.boardaround.data.entities.Post
import com.boardaround.ui.screens.ShowNewPostScreen

class PostRepository(private val postDao: PostDao) {
    suspend fun insertPost(post: Post) = postDao.insertPost(post)
    suspend fun getAllPosts() = postDao.getAllPosts()

}
