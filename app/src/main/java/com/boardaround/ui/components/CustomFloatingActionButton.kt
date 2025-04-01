package com.boardaround.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.boardaround.ui.theme.ButtonColor

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = ButtonColor,
        contentColor = Color.White,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "+",
            tint = Color.Black
        )
    }
}