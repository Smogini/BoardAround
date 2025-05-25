package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.TriviaQuestion
import com.boardaround.data.repositories.TriviaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TriviaViewModel(private val repository: TriviaRepository) : ViewModel() {

    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> = _questions

    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints

    fun correctAnswer() = _userPoints.value++

    fun loadTriviaQuestions(
        amount: Int? = 10, category: Int? = 16, difficulty: String? = null) {
        viewModelScope.launch {
            try {
                val questions = repository.getTriviaQuestions(amount, category, difficulty)
                _questions.value = questions
            } catch (e: Exception) {
                Log.e("TriviaViewModel", "Error loading questions: ${e.message}", e)
                _questions.value = emptyList()
            }
        }
    }

}