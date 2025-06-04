package com.boardaround.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.screens.ToolsMenu

@Composable
fun BottomBar(navController: NavController) {
    val iconColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .align(Alignment.BottomCenter)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(60.dp),
                    ambientColor = iconColor.copy(alpha = 0.3f),
                    spotColor = iconColor.copy(alpha = 0.2f)
                ),
            actions = {
                CustomClickableIcon(
                    "Homepage",
                    icon = Icons.Filled.Home,
                    iconColor = iconColor,
                    onClick = { navController.navigate(Route.Homepage) },
                )

                Spacer(modifier = Modifier.weight(1f))

                ToolsMenu(navController)

                Spacer(modifier = Modifier.weight(0.1f))

                CustomClickableIcon(
                    title = "Account",
                    icon = Icons.Filled.AccountCircle,
                    iconColor = iconColor,
                    onClick = { navController.navigate(Route.MyProfile) }
                )
            }
        )
    }
}
