package com.boardaround.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CustomTitle(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    color: Color = MaterialTheme.colorScheme.secondary,
    alignment: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        style = textStyle.copy(
            fontWeight = FontWeight.Bold,
            color = color,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.2f),
                offset = Offset(1f, 1f),
                blurRadius = 2f
            )
        ),
        modifier = modifier.fillMaxWidth(),
        textAlign = alignment,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}