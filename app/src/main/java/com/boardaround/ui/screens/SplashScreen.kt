package com.boardaround.ui.screens

import android.os.Bundle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Usa sp per il TextUnit
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import kotlinx.coroutines.delay
import com.boardaround.R // Se il logo è una risorsa nell'app, usa questo import

@Composable
fun SplashScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    // Animazione per espandere il testo "Benvenuto su BoardAround"
    val textSize by animateFloatAsState(
        targetValue = if (isLoading) 24f else 32f, // Usa float per animare
        animationSpec = tween(durationMillis = 1000)
    )

    // Comincia il delay e successivamente naviga alla schermata successiva
    LaunchedEffect(isLoading) {
        delay(3000) // Tempo di attesa di 3 secondi
        isLoading = false
        navController.navigate(Route.Homepage) { // Naviga alla schermata principale dopo 3 secondi
            launchSingleTop = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Se hai un logo, sostituisci "R.drawable.logo" con il tuo logo
                Image(
                    painter = painterResource(id = R.drawable.logo), // Aggiungi il tuo logo qui
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Testo di benvenuto con animazione di aumento dimensione
                Text(
                    text = "Benvenuto su BoardAround!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = textSize.sp, // Converti il float in sp
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Indicatore di caricamento
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
