package com.boardaround.ui.screens.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.R
import com.boardaround.ui.screens.ScreenTemplate
import kotlin.random.Random

@Composable
fun ShowCoinFlip(navController: NavController) {
    ScreenTemplate(
        title = "Testa o Croce",
        navController = navController,
    ) {
        CoinFlipContent()
    }
}

@Composable
fun CoinFlipContent() {
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result ?: "Lancia la moneta!",
            style = MaterialTheme.typography.headlineMedium
        )

        result?.let {
            val imageRes = if (it == "Testa") R.drawable.testa else R.drawable.croce
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = it,
                modifier = Modifier.size(120.dp)
            )
        }

        Button(onClick = {
            result = if (Random.nextBoolean()) "Testa" else "Croce"
        }) {
            Text("Lancia")
        }
    }
}
