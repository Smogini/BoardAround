package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val name: String,
    val email: String,
    val password: String,
    val dob: String,
)