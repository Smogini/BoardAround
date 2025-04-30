package com.boardaround.ui.screens

import android.Manifest
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.data.entities.User
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.AuthViewModel
import java.time.LocalDateTime

@Composable
fun ShowRegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val currentContext = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val dobState = remember { mutableStateOf("Seleziona la data") }
    val showDatePicker = remember { mutableStateOf(false) }

    var registrationError by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = currentContext.contentResolver
            val takeFlags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION

            try {
                contentResolver.takePersistableUriPermission(it, takeFlags)
            } catch (e: SecurityException) {
                Log.e("ImageAccess", "Errore nel prendere il permesso persistente: ${e.message}")
            }

            selectedImageUri = it
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(currentContext, "Permesso necessario per selezionare una foto", Toast.LENGTH_SHORT).show()
        }
    }

    ScreenTemplate(
        title = "Crea un nuovo profilo",
        currentRoute = Route.Register,
        navController,
        showBottomBar = false
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Image(
                    painter = selectedImageUri?.let {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(currentContext)
                                .data(it)
                                .build()
                        )
                    } ?: painterResource(id = R.drawable.default_profile),
                    contentDescription = "Immagine profilo",
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                )

                Text(
                    text = "Clicca sull'immagine per modificarla",
                    textAlign = TextAlign.Center,
                    color = PrimaryBrown,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Username", textAlign = TextAlign.Center, color = PrimaryBrown)
                CustomTextField(label = "Username", value = usernameState.value, onValueChange = { usernameState.value = it })

                Text("Nome", textAlign = TextAlign.Center, color = PrimaryBrown)
                CustomTextField(label = "Nome", value = nameState.value, onValueChange = { nameState.value = it })

                Text("Email", textAlign = TextAlign.Center, color = PrimaryBrown)
                CustomTextField(label = "Email", value = emailState.value, onValueChange = { emailState.value = it })

                Text("Data di nascita", textAlign = TextAlign.Center, color = PrimaryBrown)
                CustomButton(onClick = { showDatePicker.value = true }, text = dobState.value)

                if (showDatePicker.value) {
                    DateTimePicker(
                        initialDateTime = LocalDateTime.now(),
                        onDateTimeSelected = { selectedDate, formattedDate ->
                            val today = LocalDateTime.now()
                            val minAllowedDate = today.minusYears(12)

                            if (selectedDate.isAfter(minAllowedDate)) {
                                Toast.makeText(currentContext, "Devi avere almeno 12 anni", Toast.LENGTH_SHORT).show()
                            } else {
                                dobState.value = formattedDate
                            }
                            showDatePicker.value = false
                        },
                        onDismiss = { showDatePicker.value = false },
                        showTimePicker = false
                    )
                }

                Text("Password", textAlign = TextAlign.Center, color = PrimaryBrown)
                CustomTextField(
                    label = "Password",
                    value = passwordState.value,
                    isPasswordField = true,
                    onValueChange = { passwordState.value = it }
                )

                CustomButton(
                    onClick = {
                        if (usernameState.value.text.isBlank() ||
                            nameState.value.text.isBlank() ||
                            emailState.value.text.isBlank() ||
                            passwordState.value.text.isBlank() ||
                            dobState.value.isBlank()
                        ) {
                            registrationError = true
                        } else {
                            try {
                                val newUser = User(
                                    username = usernameState.value.text,
                                    name = nameState.value.text,
                                    email = emailState.value.text,
                                    password = passwordState.value.text,
                                    dob = dobState.value,
                                    profilePic = selectedImageUri.toString()
                                )
                                authViewModel.registerUser(newUser)
                                navController.navigateSingleTop(Route.Login)
                            } catch (e: Exception) {
                                Log.e("Register", "Errore durante la registrazione: ${e.message}")
                            }
                        }
                    },
                    text = "Registrati"
                )
            }
        }
    }
    if (registrationError) {
        AlertDialog(
            onDismissRequest = { registrationError = false },
            title = { Text("Errore di registrazione") },
            text = { Text("Uno o più campi vuoti") },
            confirmButton = {
                TextButton(onClick = { registrationError = false }) {
                    Text("OK")
                }
            }
        )
    }
}