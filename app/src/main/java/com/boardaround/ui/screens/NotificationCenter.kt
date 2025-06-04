package com.boardaround.ui.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.boardaround.viewmodel.NotificationViewModel
import java.time.LocalDateTime

data class NotificationItem(
    val id: String,
    val senderUsername: String,
    val message: String,
    val timestamp: LocalDateTime
)

@Composable
fun ShowNotificationScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
}