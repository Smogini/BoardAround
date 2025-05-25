package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.UserSessionManager
import com.boardaround.data.entities.Post
import com.boardaround.data.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts

    private val _selectedPost = MutableStateFlow<Post?>(null)

    fun selectPost(selected: Post) {
        _selectedPost.value = selected
    }

    fun insertPost(title: String, content: String, imageUri: String?) {
        viewModelScope.launch {
            val currentUserUid = sessionManager.getCurrentUser()?.uid ?: return@launch
            val post = Post(
                title = title,
                content = content,
                imageUri = imageUri,
                author = currentUserUid
            )
            repository.insertPost(post)
        }
    }


    fun getPostsByUsername() {
        viewModelScope.launch {
            val username = sessionManager.getCurrentUser()?.username.toString()
            _userPosts.value = repository.getPostsByUsername(username)
        }
    }
}
