package com.boardaround.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.ButtonColor

@Composable
fun BottomBar(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomAppBar(
            containerColor = BottomBar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .align(Alignment.BottomCenter)
                .wrapContentHeight(),
            actions = {
                CustomButtonIcon(
                    "Homepage",
                    icon = Icons.Filled.Home,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.Homepage) {
                            launchSingleTop = true
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                CustomButtonIcon(
                    title = "Location",
                    icon = Icons.Filled.AddLocation,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.Map) {
                            launchSingleTop = true
                        }
                    }
                )

                CustomButtonIcon(
                    title = "Account",
                    icon = Icons.Filled.AccountCircle,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.MyProfile) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        )

        CustomFloatingActionButton(
            onClick = {
                /* TODO */
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(60.dp)
        )
    }
}
