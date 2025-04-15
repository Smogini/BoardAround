package com.boardaround.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.Errors
import com.boardaround.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
    userViewModel: UserViewModel? = null,
    currentRoute: Route
) {
    val hasNotifications = userViewModel?.hasNewNotifications?.collectAsState()?.value ?: false

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            },

            // Aggiungiamo l'icona a sinistra (navigationIcon)
            navigationIcon = {
                CustomButtonIcon(
                    title = "Game",
                    icon = Icons.Filled.Favorite,  // Qui usiamo un'icona ImageVector
                    iconColor = BottomBar,
                    onClick = {
                        navController.navigateSingleTop(Route.Gamification) // Azione del click
                    }
                )
            },

            actions = {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val actionButtons = remember { calculateActionButtons(currentRoute, hasNotifications) }

                    actionButtons.forEach { (title, icon) ->
                        CustomButtonIcon(
                            title = title,
                            icon = icon,
                            iconColor = if (title == "Return") Errors else BottomBar,
                            onClick = {
                                when (title) {
                                    "Settings" -> navController.navigateSingleTop(Route.EditMyProfile)
                                    "Return" -> navController.popBackStack()
                                    else -> { Log.e("Top App Bar", "Function not implemented") }
                                }
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 4.dp,
        )
    }
}



fun calculateActionButtons(currentRoute: Route, hasNotification: Boolean): List<Pair<String, ImageVector>> {
    val pagesWithoutNotifications = setOf(
        Route.Login,
        Route.Register,
        Route.EditMyProfile,
        Route.NewPost,
        Route.NewEvent
    )
    val actionButtons = mutableListOf<Pair<String, ImageVector>>()
    val notificationIcon = if(hasNotification) Icons.Filled.Notifications else Icons.Filled.NotificationsNone

    if (!pagesWithoutNotifications.contains(currentRoute)) {
        actionButtons.add("Notifications" to notificationIcon)
    }

    when (currentRoute) {
        Route.MyProfile -> actionButtons.add("Settings" to Icons.Filled.Settings)
        Route.EditMyProfile -> actionButtons.add("Return" to Icons.Filled.Cancel)
        else -> {  }
    }

    return actionButtons
}
