package com.boardaround.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.boardaround.ui.screens.InfoRow

@Composable
fun <T> ExpandableSection(
    title: String,
    icon: ImageVector,
    itemList: List<T>,
    labelProvider: (T) -> String,
    onCLick: (T) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize (
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
                .padding(8.dp)
        ) {
            Text(title)
            CustomClickableIcon(
                title = title,
                icon = icon,
                iconColor = MaterialTheme.colorScheme.primary,
                onClick = { isExpanded = !isExpanded }
            )
        }
        if (isExpanded) {
            if (itemList.isNotEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    itemList.forEach { item ->
                        InfoRow(
                            leadingIcon = Icons.Default.Star,
                            label = labelProvider(item),
                            value = "",
                            onClick = { onCLick(item) }
                        )
                    }
                }
            } else {
                Text("Nessun elemento disponibile")
            }
        }
    }
}
