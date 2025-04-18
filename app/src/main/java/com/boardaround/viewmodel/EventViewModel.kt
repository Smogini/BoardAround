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
    private val repository: EventRepository
) : ViewModel() {

    private val _eventsFound = MutableStateFlow<List<Event>>(emptyList())
    val eventsFound: StateFlow<List<Event>> = _eventsFound

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    fun selectEvent(event: Event) {
        this._selectedEvent.value = event
    }

    fun insertEvent(newEvent: Event) {
        viewModelScope.launch {
            repository.insertEvent(newEvent)
        }
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            try {
                _eventsFound.value = repository.searchEventsByName(query)
            } catch(e: Exception) {
                Log.e("EventViewModel", "Errore nella ricerca degli eventi: ${e.message}", e)
            }
        }
    }

    fun searchEventsByUsername(username: String) {
        viewModelScope.launch {
            try {
                _eventsFound.value = repository.getEventsByUsername(username)
            } catch (e: Exception) {
                Log.e("EventViewModel", "Errore nella ricerca degli eventi: ${e.message}", e)
            }
        }
    }
}
