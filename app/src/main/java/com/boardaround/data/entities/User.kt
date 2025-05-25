package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String = "",
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val dob: String = "",
    val profilePic: String = "",
    val fcmToken: String? = null,
)

@Entity(
    tableName = "friendships",
    primaryKeys = ["userUsername", "friendUsername"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["userUsername"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["friendUsername"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["friendUsername"])
    ]
)
data class Friendship(
    val userUsername: String,
    val friendUsername: String
)