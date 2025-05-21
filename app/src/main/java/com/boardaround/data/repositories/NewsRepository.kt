package com.boardaround.data.repositories

import android.util.Log
import com.boardaround.data.entities.Article
import com.boardaround.network.NewsApiService


class NewsRepository(
    private val newsApiService: NewsApiService,
    private val apiKey: String
) {

    suspend fun getBoardGameNews(language: String = "en"): Result<List<Article>> {
        val queryKeywords = "\"board games\" OR \"tabletop games\" OR \"giochi da tavolo\" OR \"giochi di società\""

        return try {
            Log.d("NewsRepository", "Tentativo di fetch notizie giochi da tavolo. Query: [$queryKeywords], Lingua: [$language]")

            val response = newsApiService.searchNews(
                query = queryKeywords,
                language = language,
                sortBy = "publishedAt",
                apiKey = this.apiKey // Usa la apiKey passata al costruttore
            )

            if (response.isSuccessful) {
                val articles = response.body()?.articles
                if (!articles.isNullOrEmpty()) {
                    Log.i("NewsRepository", "Recuperati ${articles.size} articoli sui giochi da tavolo.")
                    Result.success(articles)
                } else {
                    Log.w("NewsRepository", "Nessun articolo sui giochi da tavolo trovato o la lista è vuota. Risposta API: ${response.body()?.status}")
                    Result.success(emptyList())
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Corpo dell'errore non disponibile"
                Log.e("NewsRepository", "Errore API durante il recupero delle notizie: Codice ${response.code()} - $errorBody")
                Result.failure(Exception("Errore API ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Eccezione durante il recupero delle notizie sui giochi da tavolo: ${e.message}", e)
            Result.failure(e)
        }
    }
}