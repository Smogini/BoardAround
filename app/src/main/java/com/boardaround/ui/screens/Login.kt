package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowLoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var loginSuccess by remember { mutableStateOf(false) } // Stato per il successo del login
    val context = LocalContext.current

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            Toast.makeText(context, "Login effettuato con successo", Toast.LENGTH_SHORT).show()
            loginSuccess = false // Reset dopo aver mostrato il Toast
        }
    }

    ScreenTemplate(
        title = "Benvenuto su BoardAround!",
        currentRoute = Route.Login,
        navController,
        showBottomBar = false
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Username", textAlign = TextAlign.Center, color = PrimaryText)
            OutlinedTextField(
                label = {
                    Text(
                        "Inserisci username",
                        color = if (loginError) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = usernameState.value,
                onValueChange = { usernameState.value = it }
            )

            Text("Password", textAlign = TextAlign.Center, color = PrimaryText)
            OutlinedTextField(
                label = {
                    Text(
                        "Inserisci password",
                        color = if (loginError) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                value = passwordState.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { passwordState.value = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                    }
                }
            )

            CustomButton(
                onClick = {
                    if (usernameState.value.text.isBlank() && passwordState.value.text.isBlank()) {
                        loginError = true
                        errorMessage = "Campo username e password vuoti"
                    } else if (usernameState.value.text.isBlank()) {
                        loginError = true
                        errorMessage = "Campo username vuoto"
                    } else if (passwordState.value.text.isBlank()) {
                        loginError = true
                        errorMessage = "Campo password vuoto"
                    } else {
                        authViewModel.login(usernameState.value.text, passwordState.value.text) { isSuccess ->
                            if (isSuccess) {
                                loginSuccess = true // Imposta lo stato di successo
                                navController.navigateSingleTop(Route.Homepage)
                                loginError = false
                                errorMessage = ""
                            } else {
                                loginError = true
                                errorMessage = "Email o password non valide"
                            }
                        }
                    }
                },
                text = "Accedi"
            )
            CustomButton(
                onClick = {
                    navController.navigateSingleTop(Route.Register)
                },
                text = "Registrati"
            )

            // Popup di errore
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