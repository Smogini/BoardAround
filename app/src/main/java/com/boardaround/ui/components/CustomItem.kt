package com.boardaround.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CustomItem(
    title: String,
    description: String,
    imageUrl: String ?= "No image"
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(8.dp))

            Text(description, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(8.dp))

            imageUrl?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Post image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}