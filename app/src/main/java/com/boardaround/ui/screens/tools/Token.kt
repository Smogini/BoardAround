package com.boardaround.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.screens.ScreenTemplate

@Composable
fun ShowTokenScreen(navController: NavController) {
    ScreenTemplate(
        title = "Token",
        navController = navController
    ) {
        item {
            TokenContent()
        }
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
