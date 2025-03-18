package com.example.boardaround.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.TextFieldValue
import com.example.boardaround.ui.components.CustomButton // Importa il CustomButton
import com.example.boardaround.ui.components.CustomTextField

@Composable
fun LoginScreen(navController: NavController) {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    Column(modifier = Modifier.padding(16.dp)) {

        CustomTextField(  //Username
            label = "Username",
            value = usernameState.value,
            onValueChange = { usernameState.value = it }
        )


        CustomTextField( //Password
            label = "Password",
            value = passwordState.value,
            onValueChange = { passwordState.value = it }
        )


        CustomButton( //Accedi
            onClick = {
                navController.navigate("homepage")
            },
            text = "Accedi"
        )


        CustomButton( //Registrati
            onClick = {
                navController.navigate("registrati")
            },
            text = "Registrati"
        )
    }
}


