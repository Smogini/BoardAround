package com.example.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boardaround.ui.theme.BoardAroundTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

class HomepageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme {
                HomepageScreen()
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomepageScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Homepage", style = TextStyle(fontSize = 20.sp)) },
                    actions = {
                        // Aggiungi le icone di notifiche e profilo nella barra
                        IconButton(onClick = { /* Azione per notifiche */ }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                        }
                        IconButton(onClick = { /* Azione per profilo */ }) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Azione per FAB */ },
                    containerColor = Color(0xFFFFC107), // Colore giallo FFC107
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Item", tint = Color.White)
                }
            },
            floatingActionButtonPosition = FabPosition.Center, // Posizione FAB al centro in basso
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFEDE0D4)) // Sfondo color crema
            ) {
                // Contenuto principale della homepage
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Benvenuto nella Homepage!", style = TextStyle(fontSize = 24.sp))
                }
            }
        }
    }
}

