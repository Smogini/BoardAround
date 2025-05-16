package com.boardaround.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.theme.PrimaryBrown

@Composable
fun ShowInviteScreen(navController: NavController) {
    ScreenTemplate(
        title = "Sei stato invitato da :",
        currentRoute = Route.Invite,
        navController,
    ) {
        item {
            Text("Descrizione dell'evento", textAlign = TextAlign.Center, color = PrimaryBrown)
            Text("Gioco dell'evento", textAlign = TextAlign.Center, color = PrimaryBrown)
            Text("Partecipanti", textAlign = TextAlign.Center, color = PrimaryBrown)
            Text("Luogo dell'evento", textAlign = TextAlign.Center, color = PrimaryBrown)
            Text("Data e orario evento", textAlign = TextAlign.Center, color = PrimaryBrown)

            CustomButton(
                onClick = {
                    navController.navigateSingleTop(Route.Homepage)
                },
                text = "Si, partecipo all'evento"
            )

            CustomButton(
                onClick = {
                    navController.navigateSingleTop(Route.Homepage)
                },
                text = "Rifiuta invito"
            )

        }
    }
}
