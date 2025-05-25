package com.boardaround.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> CustomCarousel(
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    items: List<T>,
    onClick: ((T) -> Unit)? = null,
    imageUrlProvider: (T) -> String,
    labelProvider: (T) -> String
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    if (items.isEmpty()) return

    Column {
        CustomTitle(
            text = title,
            textStyle = titleStyle,
            alignment = TextAlign.Start,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items) { index, item ->
                CustomImageCard(
                    item = item,
                    onClick = {
                        selectedIndex = index
                        onClick?.invoke(item)
                    },
                    image = imageUrlProvider(item),
                    imageCaption = labelProvider(item),
                    contentDescription = title,
                    isSelected = selectedIndex == index,
                    cardSize = 100,
                    cardShape = CircleShape
                )
            }
        }
    }
}
