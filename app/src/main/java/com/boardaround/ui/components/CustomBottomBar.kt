package com.boardaround.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.ButtonColor

@Composable
fun BottomBar(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // BottomAppBar
        BottomAppBar(
            containerColor = BottomBar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 56.dp),
            actions = {
                // Pulsante a sinistra per "ArrowBack"
                IconButton(onClick = {
                    navController.navigate(Route.Login) {
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
                Spacer(modifier = Modifier.weight(1f))

                // Pulsante "AddLocation" a destra
                IconButton(onClick = {
                    navController.navigate(Route.Map) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(imageVector = Icons.Filled.AddLocation, "Location", tint = ButtonColor)
                }

                IconButton(onClick = {
                    navController.navigate(Route.MyProfile) {
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
                .align(Alignment.Center)
                .offset(y = -15.dp)
                .size(60.dp)
        )
    }
}
