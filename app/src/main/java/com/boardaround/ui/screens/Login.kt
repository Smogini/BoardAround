package com.boardaround.ui.screens

import androidx.activity.ComponentActivity
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
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.UserViewModel

class Login(private val navController: NavController, private val userViewModel: UserViewModel): ComponentActivity() {

    @Composable
    fun ShowLoginScreen() {
        val usernameState = remember { mutableStateOf(TextFieldValue()) }
        val passwordState = remember { mutableStateOf(TextFieldValue()) }

        ScreenTemplate(
            title = "Benvenuto su BoardAround!",
            navController,
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Username", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Inserisci username", value = usernameState.value, onValueChange = { usernameState.value = it })

                Text("Password", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(
                    label = "Inserisci password",
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    isPasswordField = true
                )

                CustomButton(
                    onClick = {
                        userViewModel.login(usernameState.value.text, passwordState.value.text) { result ->
                            if (result) {
                                navController.navigate(Route.Homepage) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    text = "Accedi"
                )
                CustomButton(
                    onClick = {
                        navController.navigate(Route.Register) {
                            launchSingleTop = true
                        }
                    },
                    text = "Registrati"
                )
            }
        }
    }
}