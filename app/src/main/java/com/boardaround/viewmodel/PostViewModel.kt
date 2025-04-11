package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Post
import com.boardaround.data.repositories.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    fun insertPost(title: String, content: String, imageUri: String?) {
        val post = Post(title = title, content = content, imageUri = imageUri)
        viewModelScope.launch {
            repository.insertPost(post)
        }
    }


}
