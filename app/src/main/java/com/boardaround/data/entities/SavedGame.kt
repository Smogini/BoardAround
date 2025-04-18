package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_game")
data class SavedGame(
    @PrimaryKey val id: Int,
    val user: String,
    val name: String,
    val yearPublished: Int?,
    val imageUrl: String ?= "No image",
    val description: String ?= "No description"
)

fun Game.toSavedGame(user: String): SavedGame = SavedGame(
    id = this.id,
    user = user,
    name = this.nameElement.value,
    yearPublished = this.yearPublished?.value,
    imageUrl = this.imageUrl,
    description = this.description
)

