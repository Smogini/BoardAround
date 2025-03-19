package com.boardaround.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Homepage : Route("homepage")
}


