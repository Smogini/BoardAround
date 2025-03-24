package com.boardaround.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Homepage : Route("homepage")
    data object Invite : Route("invite")
    data object NewEvent : Route("newEvent")
    data object Profile : Route("profile")
    data object MyProfile : Route("myProfile")
    data object EditMyProfile : Route("editMyProfile")
    data object GameInfo : Route("gameInfo")
    data object Map : Route("map")
}


