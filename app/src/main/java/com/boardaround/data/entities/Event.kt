package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "events",
    primaryKeys = ["name", "author"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["author"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("author")]
)
data class Event(
    val name: String,
    val author: String,
    val description: String,
    val address: String,
    val dateTime: String,
    val isPrivate: Boolean,
    val imageUrl: String? = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)