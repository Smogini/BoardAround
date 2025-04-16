package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.trivia.TriviaQuestion
import com.boardaround.data.repositories.TriviaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TriviaViewModel : ViewModel() {
    private val repository = TriviaRepository()
    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> = _questions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadTriviaQuestions(amount: Int = 10, category: Int? = null, difficulty: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val questions = repository.getTriviaQuestions(amount, category, difficulty)
                _questions.value = questions
            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento delle domande: ${e.message}"
                _questions.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}