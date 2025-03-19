package com.boardaround.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.ButtonColor

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        containerColor = BottomBar,
        modifier = Modifier.padding(10.dp).wrapContentHeight().clip(RoundedCornerShape(50.dp)),
        actions = {
            CustomButtonIcon(
                "Homepage",
                icon = Icons.Filled.Home,
                ButtonColor,
                onClick = { navController.navigate(Route.Homepage.route) {
                    launchSingleTop = true
                }},
            )
            Spacer(modifier = Modifier.weight(1f))

            CustomButtonIcon(
                "Map",
                icon = Icons.Filled.LocationOn,
                ButtonColor,
                onClick = { navController.navigate("TODO") {
                    launchSingleTop = true
                }
            })

            CustomButtonIcon(
                "Account",
                icon = Icons.Filled.AccountCircle,
                ButtonColor,
                onClick = { navController.navigate("myProfile") {
                    launchSingleTop = true
                }
            })
        },
    )
}