package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryText

class NewEvent (private val navController: NavController) {

    @Composable
    fun ShowNewEventScreen() {
        val eventState = remember { mutableStateOf(TextFieldValue()) }
        val descriptionState = remember { mutableStateOf(TextFieldValue()) }

        ScreenTemplate(
            title = "Crea nuovo evento",
            navController,
        ) {
            Column {
                Text("Inserisci nome evento", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Inserisci nome evento", value = eventState.value, onValueChange = { eventState.value = it })

                Text("Inserisci descrizione", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Inserisci descrizione", value = descriptionState.value, onValueChange = { descriptionState.value = it })
            }
        }
    }
}