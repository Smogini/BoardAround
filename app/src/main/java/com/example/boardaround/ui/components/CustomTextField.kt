package com.example.boardaround.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun CustomTextField(
    label: String, // Testo della label
    value: TextFieldValue, // Stato del valore di input
    onValueChange: (TextFieldValue) -> Unit // Funzione per gestire il cambiamento del valore
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth() // Il campo di testo prende tutta la larghezza disponibile
            .padding(16.dp) // Padding interno per separare il campo dal bordo
            .padding(12.dp), // Padding interno per il contenuto del campo
        shape = RoundedCornerShape(20.dp), // Arrotondamento degli angoli con un valore di 8dp
        singleLine = true // Il campo sarà su una sola riga
    )
}
