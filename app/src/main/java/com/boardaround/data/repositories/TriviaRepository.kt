package com.boardaround.data.repositories

import android.util.Log
import androidx.core.text.HtmlCompat
import com.boardaround.data.entities.TriviaQuestion
import com.boardaround.network.ApiService

class TriviaRepository {

    suspend fun getTriviaQuestions(amount: Int?, category: Int?, difficulty: String?): List<TriviaQuestion> {
        return try {
            val response = ApiService.triviaApi.getTriviaQuestions(amount, category, difficulty)
            if (response.responseCode == 0) {
                return response.results.map { question ->
                    question.copy(
                        question = HtmlCompat.fromHtml(question.question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("TriviaRepository", "Error getting trivia questions: ${e.message}", e)
            emptyList()
        }
    }

}