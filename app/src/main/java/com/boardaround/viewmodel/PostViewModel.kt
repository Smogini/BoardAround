package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Post
import com.boardaround.data.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    fun selectPost(selected: Post) {
        _selectedPost.value = selected
    }

    fun insertPost(title: String, content: String, imageUri: String?, author: String) {
        viewModelScope.launch {
            val post = Post(
                title = title,
                content = content,
                imageUri = imageUri,
                author = author
            )
            repository.insertPost(post)
        }
    }

    fun getPostsByUsername(username: String) {
        viewModelScope.launch {
            val posts = repository.getPostsByUsername(username)
            _userPosts.value = posts
        }
    }
}
