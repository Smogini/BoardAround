package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Article
import com.boardaround.data.repositories.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsUiState = MutableStateFlow(NewsUiState())
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    init {
        fetchBoardGameNews(language = "en")
    }

    private fun fetchBoardGameNews(language: String = "en") {
        viewModelScope.launch {
            _newsUiState.value = NewsUiState(isLoading = true, error = null)
            val result = newsRepository.getBoardGameNews(language = language)
            result.fold(
                onSuccess = { fetchedArticles ->
                    _newsUiState.value = NewsUiState(articles = fetchedArticles)
                },
                onFailure = { exception ->
                    Log.e("NewsViewModel", "Errore durante il recupero delle notizie: ${exception.message}", exception)
                    _newsUiState.value = NewsUiState(error = exception.message ?: "Errore sconosciuto nel caricamento delle notizie")
                }
            )
        }
    }
    fun retryFetchBoardGameNews(language: String = "en") {
        fetchBoardGameNews(language)
    }
}