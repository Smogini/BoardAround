package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryBrown
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

    ScreenTemplate(
        title = "Welcome to BoardAround!",
        currentRoute = Route.Login,
        navController,
        showBottomBar = false
    ) {
        item {
            CustomTextField(
                label = "Username",
                modifier = Modifier.fillMaxWidth(),
                value = emailState,
                onValueChange = { emailState = it }
            )

            CustomTextField(
                label = "Password",
                value = passwordState,
                onValueChange = { passwordState = it },
                modifier = Modifier.fillMaxWidth(),
                isPasswordField = true
            )

            CustomButton(
                onClick = {
                    authViewModel.login(emailState.text, passwordState.text) { user ->
                        if (user != null) {
                            authViewModel.setLoggedUser(user)
                            Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                            navController.navigateSingleTop(Route.Homepage)
                        } else {
                            Toast.makeText(context, "Login error", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                text = "Login"
            )

            Text("Or", textAlign = TextAlign.Center, color = PrimaryBrown)

            CustomButton(
                onClick = { navController.navigateSingleTop(Route.Register) },
                text = "Register"
            )

            CustomAlertDialog(
                isVisible = showErrorAlert,
                title = "Login error",
                description = errorMessage,
                onDismiss = { showErrorAlert = false },
            )

        }
    }
}