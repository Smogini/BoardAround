package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Post
import com.boardaround.data.repositories.PostRepository
import com.boardaround.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts

    private val _selectedPost = MutableStateFlow<Post?>(null)

    private val currentUser = userRepository.currentUser

    fun selectPost(selected: Post) {
        _selectedPost.value = selected
    }

    fun insertPost(title: String, content: String, imageUri: String?) {
        viewModelScope.launch {
            var currentUserUid = ""
            currentUser.collectLatest { currentUserUid = it?.uid.orEmpty() }
            val post = Post(
                title = title,
                content = content,
                imageUri = imageUri,
                author = currentUserUid
            )
            repository.insertPost(post)
        }
    }


    fun getUserPosts() {
        viewModelScope.launch {
            var username = ""
            currentUser.collectLatest { username = it?.username.orEmpty() }
            repository.getPostsByUsername(username).collectLatest { _userPosts.value = it }
        }
    }
}
