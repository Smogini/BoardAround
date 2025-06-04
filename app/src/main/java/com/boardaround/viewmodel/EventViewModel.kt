package com.boardaround.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.AchievementType
import com.boardaround.data.entities.Event
import com.boardaround.data.repositories.EventRepository
import com.boardaround.data.repositories.UserRepository
import com.boardaround.network.ApiService
import com.boardaround.network.StreetMapApiResponse
import com.boardaround.utils.AchievementManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class EventViewModel(
    private val repository: EventRepository,
    userRepository: UserRepository,
    private val achievementManager: AchievementManager
) : ViewModel() {

    private val _eventsFound = MutableStateFlow<List<Event>>(emptyList())
    val eventsFound: StateFlow<List<Event>> = _eventsFound

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _searchQuery = MutableStateFlow("")

    private val currentUser = userRepository.currentUser

    /* Wait N seconds before continuing the flow.
     * Also, if the call to the API is identical to the previous one, it ignores it.
     * Finally transforms the flow into an observable state (lazily)
     * with the list of suggestions inside (otherwise empty).
    */
    val addressSuggestions: StateFlow<List<StreetMapApiResponse>> =
        _searchQuery.debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { q ->
                flow {
                    val res = ApiService.streetApi.search(query = q)
                    emit(res)
                }.catch { emit(emptyList()) }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectEvent(event: Event) {
        this._selectedEvent.value = event
    }

    fun searchEvents(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _eventsFound.value = repository.searchEventsByName(query)
            } catch(e: Exception) {
                _errorMessage.value = "Error searching for events: ${e.message}"
            }
        }
    }

    fun fetchUserEvents() {
        viewModelScope.launch {
            try {
                var username = ""
                currentUser.collectLatest { username = it?.username.orEmpty() }
                repository.fetchUserEvents(username).collectLatest { _eventsFound.value = it }
            } catch (e: Exception) {
                _errorMessage.value = "Error searching for events: ${e.message}"
            }
        }
    }

    fun createEvent(
        name: String, author: String, authorUID: String,
        description: String, address: String, dateTime: String,
        isPrivate: Boolean, imageUrl: String, gameToPlay: String, onSuccess: () -> Unit
        ) {
        val newEvent = Event(
            timestamp = System.currentTimeMillis().toString(),
            name = name,
            author = author,
            authorId = authorUID,
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
                achievementManager.unlockAchievement(AchievementType.FirstEvent)
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
