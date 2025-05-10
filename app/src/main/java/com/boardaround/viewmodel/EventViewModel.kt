package com.boardaround.viewmodel

import android.telecom.Call
import android.util.Log
import android.view.WindowInsetsAnimation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardaround.data.entities.Event
import com.boardaround.data.repositories.EventRepository
import com.boardaround.network.NominatimClient
import com.boardaround.network.StreetMapApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _eventsFound = MutableStateFlow<List<Event>>(emptyList())
    val eventsFound: StateFlow<List<Event>> = _eventsFound

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    fun selectEvent(event: Event) {
        this._selectedEvent.value = event
    }

    suspend fun insertEvent(event: Event) {
        try {
            // Aggiungi l'evento nella collezione "events"
            firestore.collection("events")
                .add(event)  // Usando un ID automatico per ogni evento
                .await()  // Attendi che l'inserimento sia completato
        } catch (e: Exception) {
            e.printStackTrace()
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

    private val _addressSuggestions = MutableStateFlow<List<StreetMapApiResponse>>(emptyList())
    val addressSuggestions: StateFlow<List<StreetMapApiResponse>> = _addressSuggestions

    private val _selectedAddress = MutableStateFlow<StreetMapApiResponse?>(null)
    val selectedAddress: StateFlow<StreetMapApiResponse?> = _selectedAddress

    fun searchAddress(query: String) {
        if (query.isBlank()) {
            _addressSuggestions.value = emptyList()
            return
        }

        _addressSuggestions.value = emptyList()

        viewModelScope.launch {
            NominatimClient.instance.search(query).enqueue(object :
                retrofit2.Callback<List<StreetMapApiResponse>> {
                override fun onResponse(
                    call: retrofit2.Call<List<StreetMapApiResponse>>,
                    response: retrofit2.Response<List<StreetMapApiResponse>>
                ) {
                    if (response.isSuccessful) {
                        val suggestions = response.body() ?: emptyList()
                        _addressSuggestions.value = suggestions
                        Log.d("EventViewModel", "Suggerimenti ricevuti: ${suggestions.size}")
                    } else {
                        _addressSuggestions.value = emptyList()
                        Log.e("EventViewModel", "Errore nella ricerca indirizzo: ${response.code()}")
                        Log.e("EventViewModel", "Messaggio di errore API: ${response.errorBody()?.string()}")

                    }
                }

                override fun onFailure(call: retrofit2.Call<List<StreetMapApiResponse>>, t: Throwable) {
                    _addressSuggestions.value = emptyList()
                    Log.e("EventViewModel", "Errore di rete nella ricerca indirizzo: ${t.message}", t)
                }
            })
        }
    }

    fun selectAddressSuggestion(suggestion: StreetMapApiResponse) {
        _selectedAddress.value = suggestion
        _addressSuggestions.value = emptyList() // Nascondi i suggerimenti dopo la selezione
    }

    fun clearSelectedAddress() {
        _selectedAddress.value = null
    }

    fun searchEventsByAddress(address: String) {
        viewModelScope.launch {
            try {
                _eventsFound.value = repository.searchEventsByAddress(address)
            } catch (e: Exception) {
                Log.e("EventViewModel", "Errore nella ricerca per indirizzo: ${e.message}", e)
            }
        }
    }

    fun createEventWithGeocoding(event: Event) {
        viewModelScope.launch {
            try {
                val response = NominatimClient.instance.search(event.address).execute()
                if (response.isSuccessful) {
                    val results: List<StreetMapApiResponse>? = response.body()
                    val firstResult = results?.firstOrNull()

                    val latitude = firstResult?.lat?.toDoubleOrNull()
                    val longitude = firstResult?.lon?.toDoubleOrNull()

                    val updatedEvent = if (latitude != null && longitude != null) {
                        event.copy(latitude = latitude, longitude = longitude)
                    } else event

                    repository.insertEvent(updatedEvent)
                } else {
                    repository.insertEvent(event)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                repository.insertEvent(event)
            }
        }
    }

    fun sendEventInvitations(event: Event, invitedUsernames: List<String>) {
        viewModelScope.launch {
            try {
                // Delega la logica di registrazione degli inviti e attivazione notifiche al Repository
                repository.sendEventInvitations(event, invitedUsernames)
                Log.d("EventViewModel", "Richiesta di invio inviti evento delegata al Repository.")
            } catch (e: Exception) {
                Log.e("EventViewModel", "Errore durante l'invio degli inviti evento nel ViewModel", e)
                // Gestisci l'errore (potresti voler mostrare un messaggio all'utente)
            }
        }
    }
}

