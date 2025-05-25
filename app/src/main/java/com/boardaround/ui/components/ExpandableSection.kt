package com.boardaround.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> ExpandableSection(
    title: String,
    icon: ImageVector,
    itemList: List<T>,
    labelProvider: (T) -> String,
    onItemClick: (T) -> Unit = {},
    trailingIcon: @Composable ((T) -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 500), label = "rotationAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .animateContentSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = { isExpanded = !isExpanded }
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(8.dp))

            CustomTitle(
                text = title,
                textStyle = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                alignment = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            CustomClickableIcon(
                title =
                    if (isExpanded) "Collapse" else "Expand",
                icon = Icons.Default.ExpandMore,
                iconColor = MaterialTheme.colorScheme.onBackground,
                onClick = {},
                rotationAngle = rotationAngle
            )
        }

        if (isExpanded) {
            if (itemList.isEmpty()) {
                CustomTitle(
                    text = "No elements available",
                    textStyle = MaterialTheme.typography.bodyMedium,
                    alignment = TextAlign.Start
                )
                return
            }
            itemList.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = labelProvider(item),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    trailingIcon?.invoke(item)
                }
            }
        }
    }
}
