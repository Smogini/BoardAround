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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.AuthViewModel

@Composable
fun ShowRegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val dobState = remember { mutableStateOf("") }

    // Launcher per scegliere immagine
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Launcher per chiedere permesso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permesso necessario per selezionare una foto", Toast.LENGTH_SHORT).show()
        }
    }

    ScreenTemplate(
        title = "Crea un nuovo profilo",
        currentRoute = Route.Register,
        navController,
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                // Immagine profilo
                Image(
                    painter = selectedImageUri?.let {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(it)
                                .build()
                        )
                    } ?: painterResource(id = R.drawable.default_profile), // immagine di default
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

                Text("Password", textAlign = TextAlign.Center, color = PrimaryText)
                CustomTextField(label = "Password", value = passwordState.value, onValueChange = { passwordState.value = it })

                CustomButton(
                    onClick = {
                        try {
                            val newUser = User(
                                username = usernameState.value.text,
                                name = nameState.value.text,
                                email = emailState.value.text,
                                password = passwordState.value.text,
                                dob = dobState.value,
                                // Qui potresti anche salvare selectedImageUri.toString() se vuoi associare l'immagine
                            )
                            authViewModel.registerUser(newUser) {
                                navController.navigate(Route.Login) {
                                    launchSingleTop = true
                                }
                            }
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
