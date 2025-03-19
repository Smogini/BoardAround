package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.ScreenTemplate
import com.boardaround.ui.theme.PrimaryText

class Invite(private val navController: NavController) {

    @Composable
    fun ShowInviteScreen() {
        ScreenTemplate(
            title = "Sei stato invitato da : "
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Descrizione dell'evento", textAlign = TextAlign.Center, color = PrimaryText)
                Text("Gioco dell'evento", textAlign = TextAlign.Center, color = PrimaryText)
                Text("Partecipanti", textAlign = TextAlign.Center, color = PrimaryText)
                Text("Luogo dell'evento", textAlign = TextAlign.Center, color = PrimaryText)
                Text("Data e orario evento", textAlign = TextAlign.Center, color = PrimaryText)

                CustomButton(
                    onClick = {
                        navController.navigate(Route.Homepage.route) {
                            launchSingleTop = true
                        }
                    },
                    text = "Si, partecipo all'evento"
                )

                CustomButton(
                    onClick = {
                        navController.navigate(Route.Homepage.route) {
                            launchSingleTop = true
                        }
                    },
                    text = "Rifiuta invito"
                )

            }


        }

    }
}