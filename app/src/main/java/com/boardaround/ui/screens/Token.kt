package com.boardaround.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route

@Composable
fun TokenScreen(navController: NavController) {
    ScreenTemplate(
        title = "Token",
        currentRoute = Route.Token,
        navController = navController,
        showBottomBar = true
    ) {
        TokenContent()
    }
}

@Composable
fun TokenContent() {
    var tokenCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Token: $tokenCount",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { if (tokenCount > 0) tokenCount-- },
                enabled = tokenCount > 0
            ) {
                Text("-1")
            }
            Button(onClick = { tokenCount++ }) {
                Text("+1")
            }
            Button(onClick = { tokenCount = 0 }) {
                Text("Reset")
            }
        }
    }
}
