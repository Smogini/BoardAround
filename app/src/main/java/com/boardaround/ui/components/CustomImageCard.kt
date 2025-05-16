package com.boardaround.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun <T> CustomImageCard(
    item: T,
    onClick: (T) -> Unit,
    aspectRatio: Float = 1f,
    image: String,
    imageCaption: String = "",
    contentDescription: String,
    cardSize: Int = 110
) {
    Box(
        modifier = Modifier
            .size(cardSize.dp)
            .padding(end = 5.dp)
    ) {
        val shape = RoundedCornerShape(50.dp)

        Card(
            shape = shape,
            onClick = { onClick.invoke(item) },
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(aspectRatio)
        ) {
            Box {
                AsyncImage(
                    model = image,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = shape)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = imageCaption,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

        }
    }
}