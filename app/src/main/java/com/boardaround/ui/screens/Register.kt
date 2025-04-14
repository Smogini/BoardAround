package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
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
import androidx.compose.material3.Text
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
import com.boardaround.ui.theme.PrimaryText
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
    val dobState = remember { mutableStateOf<LocalDateTime?>(null) }
    val showDatePicker = remember { mutableStateOf(false) }
    val formattedDateTime = remember { mutableStateOf("Seleziona la data") }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                )

                Text(
                    text = "Clicca sull'immagine per modificarla",
                    textAlign = TextAlign.Center,
                    color = PrimaryText,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Username", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Username", value = usernameState.value, onValueChange = { usernameState.value = it })

                Text("Nome", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Nome", value = nameState.value, onValueChange = { nameState.value = it })

                Text("Email", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Email", value = emailState.value, onValueChange = { emailState.value = it })

                Text("Data di nascita", textAlign = TextAlign.Center, color = PrimaryText)
                CustomButton(onClick = { showDatePicker.value = true }, text = formattedDateTime.value)

                if (showDatePicker.value) {
                    DateTimePicker(
                        initialDateTime = null,
                        onDateTimeSelected = { selectedDate, format ->
                            dobState.value = selectedDate
                            formattedDateTime.value = format
                            showDatePicker.value = false
                        },
                        onDismiss = { showDatePicker.value = false },
                        showTimePicker = false
                    )
                }

                Text("Password", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(
                    label = "Password",
                    value = passwordState.value,
                    isPasswordField = true,
                    onValueChange = { passwordState.value = it }
                )

                CustomButton(
                    onClick = {
                        try {
                            val newUser = User(
                                username = usernameState.value.text,
                                name = nameState.value.text,
                                email = emailState.value.text,
                                password = passwordState.value.text,
                                dob = dobState.component1().toString(),
                                profilePic = selectedImageUri.toString()
                            )
                            authViewModel.registerUser(newUser)
                            navController.navigateSingleTop(Route.Login)
                        } catch (e: Exception) {
                            Log.e("Register", "Errore durante la registrazione: ${e.message}")
                        }
                    },
                    text = "Registrati"
                )
            }
        }
    }
}
