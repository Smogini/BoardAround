package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: String = "",
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val dob: String = "",
    val profilePic: String? = null,
    val fcmToken: String? = null,
)