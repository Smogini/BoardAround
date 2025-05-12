package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement")
data class Achievement(
    @PrimaryKey val id: Int = -1,
    val description: String = "No description",
    val isUnlocked: Boolean = false
)