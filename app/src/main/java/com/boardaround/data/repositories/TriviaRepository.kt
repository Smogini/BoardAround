package com.boardaround.data.repositories

import com.boardaround.data.trivia.TriviaQuestion
import com.boardaround.network.RetrofitInstance

class TriviaRepository {
    suspend fun getTriviaQuestions(amount: Int = 10, category: Int? = null, difficulty: String? = null): List<TriviaQuestion> {
        return try {
            val response = RetrofitInstance.triviaApi.getTriviaQuestions(amount, category, difficulty)
            if (response.response_code == 0) { // 0 indica successo in OTDB
                response.results
            } else {
                // Gestisci il codice di errore (log, restituisci una lista vuota, ecc.)
                emptyList()
            }
        } catch (e: Exception) {
            // Gestisci l'eccezione (log, restituisci una lista vuota, ecc.)
            emptyList()
        }
    }
}