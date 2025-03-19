package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.ScreenTemplate
import com.boardaround.ui.theme.PrimaryText
import androidx.activity.ComponentActivity


class NewEvent (private val navController: NavController) {

    @Composable
    fun ShowNewEventScreen() {
        val eventState = remember { mutableStateOf(TextFieldValue()) }
        val descriptionState = remember { mutableStateOf(TextFieldValue()) }

        ScreenTemplate(
            title = "Crea nuovo evento"
        ) {
            Column(
            ) {
                Text("Inserisci nome evento", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Inserisci nome evento", value = eventState.value, onValueChange = { eventState.value = it })

                Text("Inserisci descrizione", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Inserisci descrizione", value = descriptionState.value, onValueChange = { descriptionState.value = it })
            }
        }
    }
}