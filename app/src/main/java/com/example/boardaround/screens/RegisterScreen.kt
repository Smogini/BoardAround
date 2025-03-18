package com.example.boardaround.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import com.example.boardaround.ui.components.CustomButton // Importa il CustomButton
import com.example.boardaround.ui.components.CustomTextField

@Composable
fun RegisterScreen() {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue) }

    Column(modifier = Modifier.padding(16.dp)) {
        // TextField per l'username
        CustomTextField(
            label = "Username",
            value = usernameState.value,
            onValueChange = { usernameState.value = it }
        )



        // TextField per la password
        CustomTextField(
            label = "Password",
            value = passwordState.value,
            onValueChange = { passwordState.value = it }
        )

        // Bottone "Accedi" usando CustomButton
        CustomButton(
            onClick = {

            },
            text = "Accedi"
        )


        CustomButton(
            onClick = {
                // Logica della registrazione
            },
            text = "Registrati"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}

