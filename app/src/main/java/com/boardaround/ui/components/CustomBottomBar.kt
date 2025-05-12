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
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.screens.ToolsMenu

@Composable
fun BottomBar(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .padding(bottom = 15.dp)
                .align(Alignment.BottomCenter)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(60.dp)),
            actions = {
                CustomButtonIcon(
                    "Homepage",
                    icon = Icons.Filled.Home,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { navController.navigateSingleTop(Route.Homepage) }
                )

                Spacer(modifier = Modifier.weight(1f))

                ToolsMenu(navController)

                Spacer(modifier = Modifier.weight(0.1f))

                CustomButtonIcon(
                    title = "Account",
                    icon = Icons.Filled.AccountCircle,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { navController.navigateSingleTop(Route.MyProfile) }
                )
            }
        )
    }
}
