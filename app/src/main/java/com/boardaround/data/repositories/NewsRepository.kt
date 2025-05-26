package com.boardaround.data.repositories

import com.boardaround.data.entities.Article
import com.boardaround.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewsRepository {

    private val apiKey = "0eaaf73276024761bf5c7b2a63083ca6"

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    suspend fun getBoardGameNews(language: String = "en"): List<Article> {
        val queryKeywords = "\"boardgames\" OR \"tabletop games\""

        try {
            val response = ApiService.newsApi.searchNews(
                query = queryKeywords,
                language = language,
                sortBy = "publishedAt",
                apiKey = this.apiKey
            )

            if (response.articles!!.isNotEmpty()) {
                return response.articles
            }
        } catch (e: Exception) {
            _errorMessage.value = "Exception while recovering news about board games: ${e.message}"
        }

        return emptyList()
    }
}