package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boardaround.data.entities.Event

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp) // Usa CardDefaults.elevation per l'elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titolo del post
            Text(text = event.name, style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(8.dp))

            // Contenuto del post
            Text(text = event.description, style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))


            }
        }
    }
