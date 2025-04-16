package com.boardaround.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameRulesDialog(gameName: String, rules: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = gameName) },
        text = { Text(text = rules) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Chiudi")
            }
        }
    )
}