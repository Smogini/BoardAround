package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "event",
    primaryKeys = ["timestamp", "authorId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("authorId")]
)
data class Event(
    val timestamp: String,
    val name: String,
    val author: String,
    val authorId: String, /* To be able to delete from firebase correctly */
    val description: String,
    val address: String,
    val dateTime: String,
    val isPrivate: Boolean,
    val imageUrl: String,
    val gameName: String
)