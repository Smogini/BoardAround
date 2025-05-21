package com.boardaround.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Login : Route
    @Serializable data object Register : Route
    @Serializable data object Homepage : Route
    @Serializable data object Invite : Route
    @Serializable data object NewEvent : Route
    @Serializable data object Profile : Route
    @Serializable data object MyProfile : Route
    @Serializable data object EditMyProfile : Route
    @Serializable data object GameInfo : Route
    @Serializable data object NewPost : Route
    @Serializable data object EventInfo : Route
    @Serializable data object Gamification : Route
    @Serializable data object UtilTools: Route
    @Serializable data object NotificationCenter: Route
}
