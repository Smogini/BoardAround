package com.boardaround.ui.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun <T> CustomImageCard(
    item: T,
    isSelected: Boolean? = false,
    onClick: ((T) -> Unit)? = null,
    image: Any,
    imageCaption: String? = null,
    contentDescription: String = "",
    cardSize: Int = 110,
    cardShape: Shape = RectangleShape,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(isSelected ?: false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "imageSelection"
    )
    val border =
        if (isPressed) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    val backgroundColor = MaterialTheme.colorScheme.surface
    val captionBackground = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .size(cardSize.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 8.dp,
                shape = cardShape,
                clip = true
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick?.invoke(item).let { isPressed = !isPressed }
                }
            )
            .fillMaxSize()
    ) {
        Card(
            shape = cardShape,
            border = border,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = image,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Log.d("image", "$imageCaption")

                if (!imageCaption.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(captionBackground)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                    ) {
                        CustomTitle(
                            text = imageCaption,
                            textStyle = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            alignment = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
