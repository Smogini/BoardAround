package com.boardaround.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomImagePicker
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowRegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var dobState by remember { mutableStateOf(TextFieldValue("Select the date")) }
    val showDatePicker = remember { mutableStateOf(false) }

    val registrationError by authViewModel.registrationError.collectAsState()
    var showErrorAlert by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }

    LaunchedEffect(registrationError) {
        showErrorAlert = registrationError.isNotBlank()
    }

    ScreenTemplate(
        title = "Create a new profile",
        currentRoute = Route.Register,
        navController = navController,
        showBottomBar = false
    ) {
        item {
            Spacer(Modifier.height(10.dp))

            CustomTitle(text = "Click on the image to edit it")
            /* TODO: chiedere permesso all'utente */
            CustomImagePicker(
                onImageSelected = { path ->
                    selectedImageUri = Uri.parse(path)
                },
                imageContentDescription = "Profile picture"
            )
        }

        item {
            CustomTitle(text = "Username")
            CustomTextField(
                label = "Username",
                value = usernameState.value.text,
                onValueChange = { usernameState.value = TextFieldValue(it) }
            )
        }

        item {
            CustomTitle(text = "Name")
            CustomTextField(
                label = "Name",
                value = nameState.value.text,
                onValueChange = { nameState.value = TextFieldValue(it) }
            )
        }

        item {
            CustomTitle(text = "Email")
            CustomTextField(
                label = "Email",
                value = emailState.value.text,
                onValueChange = { emailState.value = TextFieldValue(it) },
                keyboardType = KeyboardType.Email
            )
        }

        item {
            CustomTitle(text = "Date of birth")
            CustomButton(onClick = { showDatePicker.value = true }, text = dobState.text)

            DateTimePicker(
                isVisible = showDatePicker.value,
                showTimePicker = false,
                onResult = { formattedDate ->
                    dobState = TextFieldValue(formattedDate)
                },
                onDismiss = { showDatePicker.value = false }
            )
        }

        item {
            CustomTitle(text = "Password")
            CustomTextField(
                label = "Password",
                value = passwordState.value.text,
                onValueChange = { passwordState.value = TextFieldValue(it) },
                keyboardType = KeyboardType.Password
            )
        }

        item {
            CustomButton(
                onClick = {
                    authViewModel.registerUser(
                        username = usernameState.value.text,
                        password = passwordState.value.text,
                        name = nameState.value.text,
                        email = emailState.value.text,
                        dob = dobState.text,
                        profilePic = selectedImageUri.toString()
                    ) {
                        navController.navigate(Route.Login)
                    }
                },
                text = "Register"
            )
        }
    }

    CustomAlertDialog(
        isVisible = showPermissionRationale,
        title = "Necessary permission",
        description =
            "To select a profile picture, you must grant access to the gallery.",
        onDismiss = { showPermissionRationale = false }
    )

    CustomAlertDialog(
        isVisible = permissionDeniedPermanently,
        title = "Permit permanently denied",
        description =
            "If you want to select a profile picture, enable permission in the settings.",
        onDismiss = { permissionDeniedPermanently = false }
    )

    CustomAlertDialog(
        isVisible = showErrorAlert,
        title = "Registration error",
        description = registrationError,
        onDismiss = {
            showErrorAlert = false
            authViewModel.cleanErrorMessage()
        }
    )
}
