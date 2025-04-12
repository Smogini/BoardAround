package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val name: String,
    val description: String,
    val address: String,
    val dateTime: String,
    val isPrivate: Boolean,
    val imageUrl: String? = "",
)