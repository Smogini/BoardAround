package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["author"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("author")]
)
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val title: String,
    val content: String,
    val imageUri: String? = null
)
