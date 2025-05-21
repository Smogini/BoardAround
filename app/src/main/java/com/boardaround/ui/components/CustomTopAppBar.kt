package com.boardaround.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.boardaround.ui.components.NotificationIconWithBadge
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
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

            navigationIcon = {
                CustomClickableIcon(
                    title = "Gamification",
                    icon = Icons.Filled.EmojiEvents,
                    iconColor = MaterialTheme.colorScheme.onBackground,
                    onClick = { navController.navigateSingleTop(Route.Gamification) }
                )
            },

            actions = {
                /* TODO: cancellare le modifiche apportate */
                if (clearEdit) {
                    AlertDialog(
                        onDismissRequest = { clearEdit = false },
                        title = { Text("Cancella") },
                        text = { Text("Sei sicuro di voler tornare indietro?") },
                        confirmButton = {
                            TextButton(onClick = { navController.navigateSingleTop(Route.MyProfile) }) {
                                Text("OK")
                            }
                        }
                    )
                }
                if (currentRoute == Route.MyProfile) {
                    CustomClickableIcon(
                        title = "Edit profile",
                        icon = Icons.Default.ManageAccounts,
                        iconColor = MaterialTheme.colorScheme.onBackground,
                        onClick = { navController.navigateSingleTop(Route.EditMyProfile) }
                    )
                }
                if (currentRoute == Route.EditMyProfile) {
                    CustomClickableIcon(
                        title = "Cancel",
                        icon = Icons.Default.Cancel,
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        onClick = { clearEdit = true }
                    )
                }
                val notificationCount = userViewModel?.unreadNotificationCount?.collectAsState()?.value ?: 0
                NotificationIconWithBadge(
                    notificationCount = notificationCount,
                    onClick = { navController.navigateSingleTop(Route.NotificationCenter) }
                )

            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 4.dp
        )
    }
}
