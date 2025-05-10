package com.boardaround.data.repositories

import com.boardaround.data.dao.PostDao
import com.boardaround.data.entities.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log

class PostRepository(private val postDao: PostDao, private val firestore: FirebaseFirestore) {

    suspend fun insertPost(post: Post){
        try {

        postDao.insertPost(post)
        Log.d("PostRepository", "Post salvato nel database locale: ${post.title}")

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

        Log.d("PostRepository", "Post salvato su Firestore: ${post.title}")

    } catch (e: Exception) {
        Log.e("PostRepository", "Errore durante l'inserimento del post", e)
        throw e
    }


    }

    suspend fun getPostsByUsername(username: String): List<Post> =
        postDao.getPostsByUsername(username)

}
