package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val uid: String,
    val name: String,
    val email: String,
    val dob: String,
    val profilePic: String,
    val fcmToken: String?,
)

@Entity(
    tableName = "friendships",
    primaryKeys = ["userUsername", "friendUsername"], // Chiave primaria composta
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["userUsername"],
            onDelete = ForeignKey.CASCADE // Se un utente viene cancellato, cancella anche le sue amicizie
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["friendUsername"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["friendUsername"]) // Indice per migliorare le performance delle query sugli amici
    ]
)
data class Friendship(
    val userUsername: String,
    val friendUsername: String
)