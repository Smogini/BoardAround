package com.boardaround.network

import com.boardaround.data.entities.TriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiInterface {
    @GET("api.php")
    suspend fun getTriviaQuestions(
        @Query("amount") amount: Int? = 10,
        @Query("category") category: Int? = 16, // Board games category
        @Query("difficulty") difficulty: String? = "medium",
        @Query("type") type: String = "multiple" // Tipo di domanda (multiple choice)
    ): TriviaResponse
}