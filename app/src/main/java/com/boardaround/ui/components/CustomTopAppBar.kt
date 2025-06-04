package com.boardaround.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
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
    var clearEdit by remember { mutableStateOf(false) }
    val iconColor = MaterialTheme.colorScheme.primary
    val excludeRoute = listOf(
        Route.Login,
        Route.Register
    )

    Column {
        CenterAlignedTopAppBar(
            title = {
                CustomTitle(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    textStyle = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.basicMarquee(
                        repeatDelayMillis = 1500,
                        velocity = 30.dp
                    )
                )
            },
            navigationIcon = {
                if (!excludeRoute.contains(currentRoute)) {
                    CustomClickableIcon(
                        title = "Gamification",
                        icon = Icons.Filled.EmojiEvents,
                        iconColor = iconColor,
                        onClick = { navController.navigate(Route.Gamification) }
                    )
                }
            },
            actions = {
                if (!excludeRoute.contains(currentRoute)) {
                    CustomAlertDialog(
                        isVisible = clearEdit,
                        title = "Cancel",
                        description = "Are you sure?",
                        onConfirm = {
                            clearEdit = false
                            navController.navigate(Route.MyProfile)
                        },
                        onDismiss = { clearEdit = false }
                    )
                    if (currentRoute == Route.MyProfile) {
                        CustomClickableIcon(
                            title = "Edit profile",
                            icon = Icons.Default.ManageAccounts,
                            iconColor = iconColor,
                            onClick = { navController.navigate(Route.EditMyProfile) },
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                    if (currentRoute == Route.EditMyProfile) {
                        CustomClickableIcon(
                            title = "Cancel",
                            icon = Icons.Default.Cancel,
                            iconColor = MaterialTheme.colorScheme.error,
                            onClick = { clearEdit = true },
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                    CustomClickableIcon(
                        title = "Notification",
                        icon =
                        if (hasNotifications) Icons.Default.Notifications
                        else Icons.Default.NotificationsNone,
                        iconColor = iconColor,
                        onClick = { navController.navigate(Route.NotificationCenter) },
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                actionIconContentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 2.dp
        )
    }
}
