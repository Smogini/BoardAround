package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val author: String = "",
    val description: String = "",
    val address: String = "",
    val dateTime: String = "",
    val isPrivate: Boolean = false,
    val imageUrl: String? = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)