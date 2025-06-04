package com.boardaround.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement")
data class Achievement(
    @PrimaryKey val id: String,
    val userId: String,
    val description: String = "No description",
    val isUnlocked: Boolean = false,
)

sealed class AchievementType(
    val id: String,
    val description: String
) {
    data object Register: AchievementType("register", "Register on BoardAround")
    data object FirstLogin: AchievementType("first_login", "Login for the first time")
    data object FirstPost: AchievementType("first_post", "Post your first photo")
    data object FirstEvent: AchievementType("first_event", "Create your first event")
    data object DarkTheme: AchievementType("dark_theme", "Activate the dark theme for the first time")
    data object InviteToEvent: AchievementType("invite_to_event", "Invite a friend to your event")
    data object AddGame: AchievementType("add_game", "Add a game to the library")
}