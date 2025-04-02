package com.boardaround.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField

class NewPost(private val navController: NavController) {
    @Composable
    fun ShowNewPostScreen() {
        var title by remember { mutableStateOf(TextFieldValue("")) }
        var content by remember { mutableStateOf(TextFieldValue("")) }

        ScreenTemplate(
            title = "Nuovo Post",
            navController = navController,
            showBottomBar = false
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(
                    label = "Titolo",
                    value = title,
                    onValueChange = { title = it }
                )
                CustomTextField(
                    label = "Contenuto",
                    value = content,
                    onValueChange = { content = it }
                )
                CustomButton(
                    onClick = {
                        // TODO: Implementa la logica per salvare il post
                        println("Titolo: ${title.text}, Contenuto: ${content.text}")
                        navController.popBackStack()
                    },
                    text = "Pubblica"
                )
            }
        }
    }
}