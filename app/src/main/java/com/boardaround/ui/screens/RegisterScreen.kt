package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField

@Composable
fun RegisterScreen() {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue) }

    Column(modifier = Modifier.padding(16.dp)) {
        CustomTextField(
            label = "Username",
            value = usernameState.value,
            onValueChange = { usernameState.value = it }
        )

        CustomTextField(
            label = "Password",
            value = passwordState.value,
            onValueChange = { passwordState.value = it }
        )

        CustomButton(
            onClick = {

            },
            text = "Accedi"
        )


        CustomButton(
            onClick = {

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

