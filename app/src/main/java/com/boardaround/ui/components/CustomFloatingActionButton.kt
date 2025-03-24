package com.boardaround.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boardaround.ui.theme.ButtonColor

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit, // Azione da eseguire quando il FAB viene cliccato
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = ButtonColor, // Colore del FAB
        contentColor = Color.White, // Colore dell'icona del FAB
        modifier = modifier.offset(y = (-50).dp) // Posiziona il FAB un po' più in alto, ma lascia la forma circolare
    ) {
        // Icona all'interno del FAB
        Icon(
            imageVector = Icons.Filled.Add, // Cambia l'icona se necessario
            contentDescription = "+",
            tint = Color.Black // Colore dell'icona all'interno del FAB
        )
    }
}

