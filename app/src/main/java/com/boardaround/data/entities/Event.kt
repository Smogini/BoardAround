package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val author: String,
    val authorUID: String, /* To be able to delete from firebase correctly */
    val description: String,
    val address: String,
    val dateTime: String,
    val isPrivate: Boolean,
    val imageUrl: String,
    val gameName: String
)