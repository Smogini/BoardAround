package com.example.boardaround.ui.components



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.boardaround.ui.theme.ButtonColor
import com.example.boardaround.ui.theme.ButtonTextColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


// Funzione per creare un bottone personalizzato con il colore definito
@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor,
            contentColor = ButtonTextColor
        ),
        modifier = Modifier
            .fillMaxWidth() // Fai in modo che il bottone prenda tutta la larghezza
            .wrapContentSize(Alignment.Center)
            .padding(16.dp) // Padding interno per separare il campo dal bordo
    ) {
        Text(text = text)
    }
}
