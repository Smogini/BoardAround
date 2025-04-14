package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.boardaround.ui.theme.PrimaryText

@Composable
fun <T> SearchResultCarousel(
    title: String,
    items: List<T>,
    onClick: ((T) -> Unit)? = null,
    imageUrlProvider: (T) -> String,
    labelProvider: (T) -> String
) {
    if (items.isEmpty()) return

    Column {
        Text(
            title,
            color = PrimaryText,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(items) { item ->
                Card(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { onClick?.invoke(item) }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = imageUrlProvider(item),
                            contentDescription = "Immagine di ${labelProvider(item)}",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = labelProvider(item),
                            modifier = Modifier.padding(16.dp).widthIn(max = 150.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
