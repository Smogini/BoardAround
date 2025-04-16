package com.boardaround.network

import com.boardaround.data.trivia.TriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("api.php")
    suspend fun getTriviaQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null, // ID della categoria (opzionale)
        @Query("difficulty") difficulty: String? = null, // "easy", "medium", "hard" (opzionale)
        @Query("type") type: String = "multiple" // Tipo di domanda (multiple choice)
    ): TriviaResponse
}