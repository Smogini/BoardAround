package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowLoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var loginError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    ScreenTemplate(
        title = "Benvenuto su BoardAround!",
        currentRoute = Route.Login,
        navController,
        showBottomBar = false
    ) {
        item {
            CustomTextField(
                label = "Username",
                modifier = Modifier.fillMaxWidth(),
                value = usernameState.value,
                onValueChange = { usernameState.value = it }
            )

            CustomTextField(
                label = "Password",
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.fillMaxWidth(),
                isPasswordField = true
            )

            CustomButton(
                onClick = {
                    when {
                        usernameState.value.text.isBlank() -> {
                            errorMessage = "Campo username vuoto"
                            loginError = true
                        }
                        passwordState.value.text.isBlank() -> {
                            errorMessage = "Campo password vuoto"
                            loginError = true
                        } else -> {
                            authViewModel.login(usernameState.value.text, passwordState.value.text)
                            { user ->
                                if (user != null) {
                                    authViewModel.setLoggedUser(user)
                                    Toast.makeText(context, "Login effettuato con successo", Toast.LENGTH_SHORT).show()
                                    navController.navigateSingleTop(Route.Homepage)
                                }
                            }
                        }
                    }
                },
                text = "Accedi"
            )

            Text("Oppure", textAlign = TextAlign.Center, color = PrimaryBrown)

            CustomButton(
                onClick = { navController.navigateSingleTop(Route.Register) },
                text = "Registrati"
            )

            if (loginError) {
                AlertDialog(
                    onDismissRequest = { loginError = false },
                    title = { Text("Errore di accesso") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        TextButton(onClick = { loginError = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}