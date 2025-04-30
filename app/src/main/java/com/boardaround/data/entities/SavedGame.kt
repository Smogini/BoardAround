package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "saved_game",
    primaryKeys = ["id", "user"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["user"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user")]
)
data class SavedGame(
    val id: Int,
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

fun SavedGame.toGame(): Game = Game(
    id = this.id,
    nameElement = NameElement(this.name),
    imageUrl = this.imageUrl,
    description = this.description,
    yearPublished = this.yearPublished?.let { YearPublished(it) }
)