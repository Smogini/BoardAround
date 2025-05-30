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