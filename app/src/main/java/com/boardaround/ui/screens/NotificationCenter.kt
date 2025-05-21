package com.boardaround.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.theme.BackgroundDarkMode
import com.boardaround.ui.theme.BackgroundLightMode
import com.boardaround.ui.theme.PrimaryBrown
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NotificationItem(
    val id: String,
    val message: String,
    val timestamp: LocalDateTime
)

@Composable
fun ShowNotificationScreen(navController: NavController) {
    ScreenTemplate(
        title = "Notification Center",
        currentRoute = Route.NotificationCenter,
        navController = navController
    ) {
        items(sampleNotifications) { notification ->
            NotificationCard(notification)
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onDeleteClick: (NotificationItem) -> Unit = {} // callback per eliminare
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val isDarkMode = com.boardaround.ui.theme.LocalIsDarkMode.current
    val backgroundColor = if (isDarkMode) BackgroundDarkMode else BackgroundLightMode

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.message,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = notification.timestamp.format(formatter),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { onDeleteClick(notification) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Elimina notifica",
                        tint = Color.Red,
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(top = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

// Sample dati fittizi da mostrare
val sampleNotifications = listOf(
    NotificationItem("1", "Mario Rossi ha pubblicato un nuovo post", LocalDateTime.now().minusHours(1)),
    NotificationItem("2", "Luisa Bianchi ti segue", LocalDateTime.now().minusDays(1)),
    NotificationItem("3", "Anna Verdi ha commentato il tuo post", LocalDateTime.now().minusMinutes(30)),
)
