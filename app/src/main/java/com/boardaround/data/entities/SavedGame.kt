package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "saved_game",
    primaryKeys = ["gameId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class SavedGame(
    val userId: String,
    val gameId: Int,
    val name: String,
    val imageUrl: String = "No image available"
)