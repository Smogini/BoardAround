package com.boardaround.data.repositories

import com.boardaround.data.dao.EventDAO
import com.boardaround.data.entities.Event

class EventRepository(private val eventDao: EventDAO) {

    suspend fun insertEvent(event: Event) = eventDao.insertEvent(event)

    suspend fun getAllEvents() = eventDao.getAllEvents()

    suspend fun getEventsByUsername(username: String): List<Event> =
        eventDao.getEventsByUsername(username)

    suspend fun searchEventsByName(name: String): List<Event> =
        eventDao.searchEventsByName(name)

}
