package com.boardaround.network

import com.boardaround.data.entities.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiInterface {
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}