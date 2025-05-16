package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.boardaround.ui.theme.PrimaryBrown

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
            text = title,
            color = PrimaryBrown,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(items) { item ->

                val painter = rememberAsyncImagePainter(model = imageUrlProvider(item))
                val intrinsicSize = painter.intrinsicSize

                val aspectRatio = if (intrinsicSize.height > 0)
                    intrinsicSize.width / intrinsicSize.height
                else 1f

                CustomImageCard(
                    item = item,
                    onClick = { onClick?.invoke(item) },
                    aspectRatio = aspectRatio,
                    image = imageUrlProvider(item),
                    imageCaption = labelProvider(item),
                    contentDescription = title
                )
            }
        }
    }
}
