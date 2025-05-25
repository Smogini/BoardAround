package com.boardaround.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTitle

@Composable
fun ShowInviteScreen(navController: NavController) {
    ScreenTemplate(
        title = "You've been invited from:",
        currentRoute = Route.Invite,
        navController,
    ) {
        item {
            CustomTitle(text = "Description of the event")
            CustomTitle(text = "Game of the event")
            CustomTitle(text = "Participants")
            CustomTitle(text = "Event location")
            CustomTitle(text = "Event date and time")

            CustomButton(
                onClick = {
                    navController.navigateSingleTop(Route.Homepage)
                },
                text = "Accept the invite"
            )

            CustomButton(
                onClick = {
                    navController.navigateSingleTop(Route.Homepage)
                },
                text = "Reject invite"
            )

        }
    }
}
