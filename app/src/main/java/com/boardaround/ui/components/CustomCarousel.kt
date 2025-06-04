package com.boardaround.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
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
    cardShape: Shape = RoundedCornerShape(12.dp),
    labelProvider: ((T) -> String)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    if (items.isEmpty()) return

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTitle(
                text = title,
                textStyle = titleStyle,
                color = MaterialTheme.colorScheme.secondary,
                alignment = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            trailingIcon?.invoke()
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
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
                    imageCaption = labelProvider?.invoke(item).toString(),
                    contentDescription = title,
                    isSelected = selectedIndex == index,
                    cardSize = 130,
                    cardShape = cardShape
                )
            }
        }
    }
}
