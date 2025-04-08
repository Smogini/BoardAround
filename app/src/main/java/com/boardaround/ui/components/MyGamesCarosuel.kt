package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.utils.GameSearchResult

@Composable
fun MyGamesCarousel(title: String, searchResult: GameSearchResult, navController: NavController) {
    if (searchResult.total <= 0) {
        Text("Nessun gioco presente", color = PrimaryText, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 20.dp))
        return
    }

    Column {
        Text(title, color = PrimaryText, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = 16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(searchResult.games ?: emptyList()) { game ->
                Card(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        navController.navigate(Route.GameInfo) { launchSingleTop = true }
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = game.imageUrl,
                            contentDescription = "Immagine di ${game.nameElement.value}",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = game.nameElement.value, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}