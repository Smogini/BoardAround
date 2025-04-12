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
    private val authViewModel: AuthViewModel // 🔥 gli passo anche l'authViewModel
) : ViewModel() {

    private val _myPosts = MutableStateFlow<List<Post>>(emptyList())
    val myPosts: StateFlow<List<Post>> = _myPosts

    fun insertPost(title: String, content: String, imageUri: String?) {
        viewModelScope.launch {
            val username = authViewModel.retrieveUsername() ?: return@launch
            val post = Post(
                title = title,
                content = content,
                imageUri = imageUri,
                author = username // 🔥 adesso passo l'autore giusto
            )
            repository.insertPost(post)
        }
    }

    fun getPostsByUser() {
        viewModelScope.launch {
            val username = authViewModel.retrieveUsername() ?: return@launch
            _myPosts.value = repository.getPostsByUsername(username)
        }
    }
}
