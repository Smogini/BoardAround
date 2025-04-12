package com.boardaround.data.repositories

import com.boardaround.data.dao.EventDAO
import com.boardaround.data.entities.Event

class EventRepository(private val eventDAO: EventDAO) {

    suspend fun newEvent(newEvent: Event) {
        eventDAO.inserEvent(newEvent)
    }

    suspend fun deleteEvent(toDelete: Event) {
        eventDAO.deleteEvent(toDelete)
    }

    suspend fun searchEvent(eventName: String): List<Event> {
        val eventFound = eventDAO.searchEvents(eventName)
        return eventFound
    }
}