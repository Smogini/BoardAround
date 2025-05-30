package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ParticipantsDialog(
    isOpen: Boolean,
    participants: List<String>,
    onDismiss: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            },
            title = {
                Text("Participants (${participants.size})")
            },
            text = {
                Column {
                    if (participants.isEmpty()) {
                        Text("No participants.")
                    } else {
                        participants.forEach {
                            Text("• $it")
                        }
                    }
                }
            }
        )
    }
}
