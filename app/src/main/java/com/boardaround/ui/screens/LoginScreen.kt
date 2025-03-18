package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boardaround.ui.components.AppBar
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.Background

@Composable
fun LoginScreen(navController: NavController = rememberNavController()) {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    Scaffold(topBar = { AppBar("Benvenuto su BoardAround!") }, containerColor = Background)
    { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding).fillMaxWidth()) {
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
                    navController.navigate("homepage")
                },
                text = "Accedi"
            )

            CustomButton(
                onClick = {
                    navController.navigate("registrati")
                },
                text = "Registrati"
            )
        }
    }
}
