package com.boardaround.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.R
import com.boardaround.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    val textSize by animateFloatAsState(
        targetValue = if (isLoading) 24f else 32f,
        animationSpec = tween(durationMillis = 1000),
        label = "Text size"
    )

    LaunchedEffect(isLoading) {
        delay(3000)
        isLoading = false
        navController.navigate(Route.Login)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Benvenuto su BoardAround!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = textSize.sp,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
