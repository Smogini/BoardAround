package com.boardaround.ui.components

import androidx.compose.foundation.layout.*
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
import com.boardaround.ui.theme.PrimaryText

data class Event(val name: String, val imageUrl: String)

@Composable
fun MyEventsCarousel(events: List<Event>, navController: NavController) {
    Column {
        Text("I miei eventi", color = PrimaryText, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = 16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(events) { event ->
                Card(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
//                        navController.navigate()
                    }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = event.imageUrl,
                            contentDescription = "Immagine di ${event.name}",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = event.name, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}