package com.example.boardaround.navigation

//Rotte
sealed class Route(val route: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Homepage : Route("homepage")

}


