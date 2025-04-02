package com.boardaround.navigation

import kotlinx.serialization.Serializable

//sealed class Route(val route: String) {
//    data object Login : Route("login")
//    data object Register : Route("register")
//    data object Homepage : Route("homepage")
//    data object Invite : Route("invite")
//    data object NewEvent : Route("newEvent")
//    data object Profile : Route("profile")
//    data object MyProfile : Route("myProfile")
//    data object EditMyProfile : Route("editMyProfile")
//    data object GameInfo : Route("gameInfo")
//    data object Map : Route("map")
//}

sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Register : Route
    @Serializable data object Homepage : Route
    @Serializable data object Invite : Route
    @Serializable data object NewEvent : Route
    @Serializable data object Profile : Route
    @Serializable data object MyProfile : Route
    @Serializable data object EditMyProfile : Route
    @Serializable data object GameInfo : Route
    @Serializable data object Map : Route
    @Serializable data object NewPost : Route
}
