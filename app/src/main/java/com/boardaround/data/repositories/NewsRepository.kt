package com.boardaround.data.repositories

import com.boardaround.data.entities.Article
import com.boardaround.network.NewsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewsRepository(
    private val newsApiService: NewsApiService,
    private val apiKey: String
) {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    suspend fun getBoardGameNews(language: String = "en"): Result<List<Article>> {
        val queryKeywords = "\"board games\" OR \"tabletop games\" OR \"giochi da tavolo\" OR \"giochi di società\""

        return try {
            val response = newsApiService.searchNews(
                query = queryKeywords,
                language = language,
                sortBy = "publishedAt",
                apiKey = this.apiKey
            )

            if (response.isSuccessful) {
                val articles = response.body()?.articles
                if (!articles.isNullOrEmpty()) {
                    Result.success(articles)
                } else {
                    _errorMessage.value = "No articles on board games found or the list is empty. API Answer: ${response.body()?.status}"
                    Result.success(emptyList())
                }
            } else {
                val errorBody = response.errorBody()?.string()
                _errorMessage.value = "API error while retrieving news: Code ${response.code()} - $errorBody"
                Result.failure(Exception("API error: ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            _errorMessage.value = "Exception while recovering news about board games: ${e.message}"
            Result.failure(e)
        }
    }
}