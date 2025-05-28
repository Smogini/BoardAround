package com.boardaround.data.repositories

import com.boardaround.data.dao.EventDAO
import com.boardaround.data.entities.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository(
    private val eventDao: EventDAO,
    private val firestore: FirebaseFirestore
) {

    suspend fun insertEvent(event: Event) {
        val newEventDocRef = firestore.collection("events").document()

        val eventData = hashMapOf(
            "id" to event.id,
            "name" to event.name,
            "author" to event.author,
            "authorUID" to event.authorUID,
            "description" to event.description,
            "address" to event.address,
            "dateTime" to event.dateTime,
            "gameToPlay" to event.gameName,
            "isPrivate" to event.isPrivate,
        )

        eventDao.insertEvent(event)
        newEventDocRef.set(eventData).await()

        val notificationTriggerData = hashMapOf(
            "eventId" to event.id,
            "eventName" to event.name,
            "eventAuthor" to event.author,
            "isPrivate" to event.isPrivate,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("newEventNotificationTriggers")
            .add(notificationTriggerData)
            .await()
    }

    suspend fun getEventsByUsername(username: String): List<Event> =
        eventDao.getEventsByUsername(username)

    suspend fun searchEventsByName(name: String): List<Event> =
        eventDao.searchEventsByName(name)

    suspend fun searchEventsByAddress(address: String): List<Event> =
        eventDao.searchEventsByAddress(address)

    suspend fun sendEventInvitations(event: Event, invitedUsernames: List<String>) {
        try{
            val batch = firestore.batch()
            val invitationsCollection = firestore.collection("eventInvitations")

            for (username in invitedUsernames) {
                val invitationData = hashMapOf(
                    "eventAuthor" to event.author,
                    "invitedUsername" to username,
                    "timestamp" to System.currentTimeMillis(),
                    "status" to "pending"
                )

                val newInvitationRef = invitationsCollection.document()
                batch.set(newInvitationRef, invitationData)
            }

            batch.commit().await()

            val notificationTriggerData = hashMapOf(
                "eventName" to event.name,
                "inviterUsername" to event.author,
                "invitedUsernames" to invitedUsernames,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("eventInvitationNotificationTriggers")
                .add(notificationTriggerData)
                .await()

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteEvent(toDelete: Event) {
        try {
            eventDao.deleteEvent(toDelete)

            /* TODO: fix the elimination of the entries of the tables */
            firestore.collection("events")
                .document(toDelete.id.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            throw e
        }
    }
}
