package com.boardaround.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.ButtonColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // BottomAppBar
        BottomAppBar(
            containerColor = BottomBar, // Colore della BottomAppBar
            modifier = Modifier
                .fillMaxWidth() // La BottomAppBar occupa tutta la larghezza
                .padding(bottom = 56.dp), // Aggiungi un po' di padding in basso per il FAB
            actions = {
                // Pulsante a sinistra per "ArrowBack"
                IconButton(onClick = {
                    navController.navigate(Route.Login.route) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        "ArrowBack",
                        tint = ButtonColor
                    )
                }

                // Spacer per spingere il FAB al centro
                Spacer(modifier = Modifier.weight(1f)) // Spacer che occupa lo spazio tra il primo pulsante e il FAB

                // Pulsante "AddLocation" a destra
                IconButton(onClick = {
                    navController.navigate(Route.Map.route) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(imageVector = Icons.Filled.AddLocation, "Location", tint = ButtonColor)
                }


                IconButton(onClick = {
                    navController.navigate(Route.MyProfile.route) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(imageVector = Icons.Filled.AccountCircle, "Account", tint = ButtonColor)
                }
            }
        )

        // FloatingActionButton centrato sopra la BottomAppBar
        CustomFloatingActionButton(
            onClick = {
            },
            modifier = Modifier
                .align(Alignment.Center) // Centra il FAB orizzontalmente e verticalmente
                .offset(y = -15.dp)
                .size(60.dp)// Spostalo più in alto (puoi regolare questo valore come desideri)
        )
        }
        }
