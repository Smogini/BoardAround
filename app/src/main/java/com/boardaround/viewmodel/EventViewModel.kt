package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.UserSessionManager
import com.boardaround.data.entities.Event
import com.boardaround.data.repositories.EventRepository
import com.boardaround.network.ApiService
import com.boardaround.network.StreetMapApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {

    private val _eventsFound = MutableStateFlow<List<Event>>(emptyList())
    val eventsFound: StateFlow<List<Event>> = _eventsFound

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _addressSuggestions = MutableStateFlow<List<StreetMapApiResponse>>(emptyList())
    val addressSuggestions: StateFlow<List<StreetMapApiResponse>> = _addressSuggestions

    fun selectEvent(event: Event) {
        this._selectedEvent.value = event
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            try {
                _eventsFound.value = repository.searchEventsByName(query)
            } catch(e: Exception) {
                _errorMessage.value = "Error searching for events: ${e.message}"
            }
        }
    }

    fun searchEventsByUsername() {
        viewModelScope.launch {
            try {
                val username = sessionManager.getCurrentUser()?.username.toString()
                _eventsFound.value = repository.getEventsByUsername(username)
            } catch (e: Exception) {
                _errorMessage.value = "Error searching for events: ${e.message}"
            }
        }
    }

    fun fetchAddressSuggestions(query: String) {
        if (query.isBlank()) {
            _errorMessage.value = "Address not valid"
            return
        }
        viewModelScope.launch {
            try {
                val response = ApiService.streetApi.search(query = query, countryCodes = "it")

                if (response.isNotEmpty()) {
                    _addressSuggestions.value = response
                }
            } catch (e: Exception) {
                _errorMessage.value = "API Failure: ${e.message}"
            }
        }
    }

    fun createEvent(
        name: String, author: String, authorUID: String,
        description: String, address: String, dateTime: String,
        isPrivate: Boolean, imageUrl: String, gameToPlay: String, onSuccess: () -> Unit
        ) {
        val newEvent = Event(
            name = name,
            author = author,
            authorUID = authorUID,
            description = description,
            address = address,
            dateTime = dateTime,
            isPrivate = isPrivate,
            imageUrl = imageUrl,
            gameName = gameToPlay
        )
        if (!isValidEvent(newEvent)) {
            _errorMessage.value = "One of the parameters is missing"
            return
        }
        viewModelScope.launch {
            try {
                repository.insertEvent(newEvent)
                onSuccess()
            } catch( e: Exception) {
                _errorMessage.value = "Error creating the event: ${e.message}"
            }
        }
    }

    fun deleteEvent(toDelete: Event) {
        viewModelScope.launch {
            repository.deleteEvent(toDelete)
        }
    }

    fun sendEventInvitations(event: Event, invitedUsernames: List<String>) {
        viewModelScope.launch {
            try {
                repository.sendEventInvitations(event, invitedUsernames)
            } catch (e: Exception) {
                _errorMessage.value = "Error sending event invitations to the ViewModel: ${e.message}"
            }
        }
    }

    private fun isValidEvent(event: Event): Boolean {
        return event.name.isNotBlank() && event.author.isNotBlank()
                && event.description.isNotBlank() && event.address.isNotBlank()
                && event.dateTime.isNotBlank()
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

}

