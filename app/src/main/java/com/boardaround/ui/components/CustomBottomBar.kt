package com.boardaround.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar

@Composable
fun BottomBar(navController: NavController) {

        BottomAppBar(
            containerColor = BottomBar,
            actions = {
                IconButton(onClick = {
                    navController.navigate(Route.Homepage.route) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Filled.Home, "Homepage")
                }
                IconButton(onClick = {
                    navController.navigate(Route.Homepage.route) {
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Filled.AddLocation, "Location")
                }

            },

        )
    }