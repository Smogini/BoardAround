package com.boardaround.data.repositories

import com.boardaround.data.dao.EventDAO
import com.boardaround.data.entities.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository(private val eventDao: EventDAO, private val firestore: FirebaseFirestore) {

    suspend fun insertEvent(event: Event) {
        try {
            eventDao.insertEvent(event)

            val newEventDocRef = firestore.collection("events").document()
            val eventId = newEventDocRef.id

            val eventData = hashMapOf(
                "id" to eventId,
                "name" to event.name,
                "author" to event.author,
                "description" to event.description,
                "address" to event.address,
                "dateTime" to event.dateTime,
                "isPrivate" to event.isPrivate,
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("events")
                .document(event.name)
                .set(eventData)
                .await()

            val notificationTriggerData = hashMapOf(
                "eventId" to eventId,
                "eventName" to event.name,
                "eventAuthor" to event.author,
                "isPrivate" to event.isPrivate,
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("newEventNotificationTriggers")
                .add(notificationTriggerData)
                .await()

        } catch (e: Exception) {
            throw e
        }
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
    }
