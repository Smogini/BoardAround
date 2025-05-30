package com.boardaround.data.entities

import com.google.firebase.Timestamp


data class Friendship(
    val fromUserId: String = "",
    val toUserId: String = "",
    val status: String = "pending",
    val timestamp: Timestamp? = null
)
