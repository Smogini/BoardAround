package com.boardaround.data.repositories

import com.boardaround.data.dao.PostDao
import com.boardaround.data.entities.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PostRepository(private val postDao: PostDao, private val firestore: FirebaseFirestore) {

    suspend fun insertPost(post: Post){
        try {

        postDao.insertPost(post)

        val postData = hashMapOf(
            "author" to post.author,
            "title" to post.title,
            "content" to post.content,
            "imageUri" to post.imageUri,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("posts")
            .add(postData)
            .await()

    } catch (e: Exception) {
        throw e
    }


    }

    suspend fun getPostsByUsername(username: String): List<Post> =
        postDao.getPostsByUsername(username)

}
