package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String = "",
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null
)
