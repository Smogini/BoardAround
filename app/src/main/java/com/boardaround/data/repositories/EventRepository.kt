package com.boardaround.data.repositories

import com.boardaround.data.dao.EventDAO
import com.boardaround.data.entities.Event
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository(private val eventDao: EventDAO, private val firestore: FirebaseFirestore) {

    suspend fun insertEvent(event: Event) {
        try {
            eventDao.insertEvent(event)
            Log.d("EventRepository", "Evento salvato nel database locale: ${event.name}")

            val newEventDocRef = firestore.collection("events").document()
            val eventId = newEventDocRef.id

            val eventData = hashMapOf(
                "id" to eventId, // Assicurati che l'ID sia generato correttamente
                "name" to event.name,
                "author" to event.author,
                "description" to event.description,
                "address" to event.address,
                "dateTime" to event.dateTime,
                "isPrivate" to event.isPrivate,
                "latitude" to event.latitude, // Assicurati che questi campi siano popolati
                "longitude" to event.longitude, // Assicurati che questi campi siano popolati
                "createdAt" to System.currentTimeMillis() // Aggiungi un timestamp di creazione
                // Aggiungi altri campi rilevanti
            )

            firestore.collection("events")
                .document(event.name) // Usa l'ID dell'evento come ID del documento
                .set(eventData)
                .await() // Attendi il completamento dell'operazione

            Log.d("EventRepository", "Evento salvato in Firestore con ID: ${eventId}")

            val notificationTriggerData = hashMapOf(
                "eventId" to eventId,
                "eventName" to event.name,
                "eventAuthor" to event.author,
                "isPrivate" to event.isPrivate, // Potrebbe servire alla Cloud Function
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("newEventNotificationTriggers")
                .add(notificationTriggerData)
                .await() // Attendi il completamento

            Log.d("EventRepository", "Documento trigger notifica nuovo evento creato in Firestore.")

        } catch (e: Exception) {
            Log.e("EventRepository", "Errore durante l'inserimento dell'evento o l'attivazione della notifica", e)
            throw e // Rilancia l'eccezione per gestirla nel ViewModel o UI se necessario
        }
        }

    suspend fun getEventsByUsername(username: String): List<Event> =
        eventDao.getEventsByUsername(username)

    suspend fun searchEventsByName(name: String): List<Event> =
        eventDao.searchEventsByName(name)

    suspend fun getAllEventsWithLocation(): List<Event> =
        eventDao.getAllEventsWithLocation()

    suspend fun searchEventsByAddress(address: String): List<Event> =
        eventDao.searchEventsByAddress(address)

    suspend fun sendEventInvitations(event: Event, invitedUsernames: List<String>) {
        try{
            val batch = firestore.batch()
            val invitationsCollection = firestore.collection("eventInvitations")

            for (username in invitedUsernames) {
                val invitationData = hashMapOf(
                    //"eventId" to event.id, // Usa l'ID univoco dell'evento
                    "eventAuthor" to event.author,
                    "invitedUsername" to username,
                    "timestamp" to System.currentTimeMillis(),
                    "status" to "pending" // Stato iniziale dell'invito
                )

                val newInvitationRef = invitationsCollection.document()
                batch.set(newInvitationRef, invitationData)
            }

            batch.commit().await()
            Log.d("EventRepository", "Inviti evento registrati in Firestore per l'evento .")

            val notificationTriggerData = hashMapOf(
                //"eventId" to event.id,
                "eventName" to event.name,
                "inviterUsername" to event.author, // L'autore dell'evento è l'invitante
                "invitedUsernames" to invitedUsernames, // Passa la lista degli invitati
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("eventInvitationNotificationTriggers") // Nuova collezione trigger per inviti specifici
                .add(notificationTriggerData) // Firestore genererà un ID univoco per questo trigger
                .await()

            Log.d("EventRepository", "Documento trigger notifica invito evento creato in Firestore.")

        } catch (e: Exception) {
            Log.e("EventRepository", "Errore durante l'invio degli inviti evento nel Repository", e)
            throw e // Rilancia l'eccezione per gestirla nel ViewModel o UI se necessario
        }
    }
    }
