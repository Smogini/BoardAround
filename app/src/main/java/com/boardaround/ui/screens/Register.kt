package com.boardaround.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowRegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val dobState = remember { mutableStateOf("") }

    ScreenTemplate(
        title = "Crea un nuovo profilo",
        currentRoute = Route.Register,
        navController,
    ) { contentPadding ->
        LazyColumn (
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text("Username", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Username", value = usernameState.value, onValueChange = { usernameState.value = it })

                Text("Nome", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "name", value = nameState.value, onValueChange = { nameState.value = it })

                Text("Email", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "email", value = emailState.value, onValueChange = { emailState.value = it })

                Text("Password", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Password", value = passwordState.value, onValueChange = { passwordState.value = it })

//                DatePicker(
//                    onDateSelected = { selectedDate ->
//                        dobState.value = formatDate(selectedDate)
//                    }
//                ) {  }

                CustomButton(
                    onClick = {
                        try {
                            val newUser = User(
                                username = usernameState.value.text,
                                name = nameState.value.text,
                                email = emailState.value.text,
                                password = passwordState.value.text,
                                dob = dobState.value,
                            )
                            authViewModel.registerUser(newUser) {
                                navController.navigate(Route.Login) {
                                    launchSingleTop = true
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("Register", "Error during user registration: ${e.message}")
                        }
                    },
                    text = "Registrati"
                )
            }
        }
    }
}

//private fun formatDate(timestamp: LocalDate): String {
//    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//    return timestamp.format(formatter)
//}