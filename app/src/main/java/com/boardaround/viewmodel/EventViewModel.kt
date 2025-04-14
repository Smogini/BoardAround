package com.boardaround.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Event
import com.boardaround.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository,
//    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _myEvent = MutableStateFlow<List<Event>>(emptyList())
    val myEvent: StateFlow<List<Event>> = _myEvent

    fun insertEvent(name: String, description: String, address: String) {
        viewModelScope.launch {
//            val username = authViewModel.retrieveUsername() ?: return@launch
            val event = Event(
                name = name,
                description = description,
                address = address,
                dateTime = "",
                isPrivate = false,
                imageUrl = ""
            )
            repository.insertEvent(event)
        }
    }

    fun getEventsByUser() {
        viewModelScope.launch {
//            val username = authViewModel.retrieveUsername() ?: return@launch
            _myEvent.value = repository.getEventsByUsername("username")
        }
    }
}
