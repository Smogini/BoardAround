package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowLoginScreen(
    context: Context,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var emailState by remember { mutableStateOf(TextFieldValue()) }
    var passwordState by remember { mutableStateOf(TextFieldValue()) }
    var showErrorAlert by remember { mutableStateOf(false) }
    val errorMessage by authViewModel.registrationError.collectAsState()

    LaunchedEffect(errorMessage) {
        showErrorAlert = errorMessage.isNotBlank()
    }

    ScreenTemplate(
        title = "Welcome to BoardAround!",
        currentRoute = Route.Login,
        navController = navController,
        showBottomBar = false
    ) {
        item {
            Spacer(Modifier.height(16.dp))

            CustomTitle(
                text = "Log in to continue",
                textStyle = MaterialTheme.typography.titleLarge,
                alignment = TextAlign.Center
            )
        }

        item {
            CustomTextField(
                label = "Username",
                modifier = Modifier.fillMaxWidth(),
                value = emailState.text,
                onValueChange = { emailState = TextFieldValue(it) }
            )

            Spacer(Modifier.height(16.dp))

            CustomTextField(
                label = "Password",
                value = passwordState.text,
                onValueChange = { passwordState = TextFieldValue(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    onClick = {
                        authViewModel.login(emailState.text.trim(), passwordState.text.trim()) {
                            Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Route.Homepage)
                        }
                    },
                    text = "Login"
                )

                CustomTitle(
                    text = "Or",
                    textStyle = MaterialTheme.typography.titleMedium
                )

                CustomButton(
                    onClick = { navController.navigate(Route.Register) },
                    text = "Register"
                )
            }
        }
    }

    CustomAlertDialog(
        isVisible = showErrorAlert,
        title = "Login error",
        description = errorMessage,
        onDismiss = {
            showErrorAlert = false
            authViewModel.cleanErrorMessage()
        },
    )
}