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
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
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
                    authViewModel.login(usernameState.value.text, passwordState.value.text) { user ->
                        if (user != null) {
                            authViewModel.setLoggedUser(user)
                            Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                            navController.navigateSingleTop(Route.Homepage)
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

            if (showErrorAlert) {
                CustomAlertDialog(
                    title = "Login error",
                    description = errorMessage,
                    onDismiss = { showErrorAlert = false },
                )
            }
        }
    }
}