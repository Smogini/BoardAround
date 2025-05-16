package com.boardaround.ui.screens

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.ui.theme.PrimaryBrown
import com.boardaround.viewmodel.AuthViewModel
import java.io.File
import java.io.FileOutputStream
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
    var showPermissionRationale by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            Log.d("ImageUpload", "URI immagine selezionata: $selectedUri")

            val fileName = "profile_pic_${System.currentTimeMillis()}.jpg"
            val destinationFile = File(currentContext.filesDir, fileName)

            try {
                currentContext.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                selectedImageUri = Uri.fromFile(destinationFile)
                Log.d("ImageDisplay", "Immagine copiata in: $selectedImageUri")

            } catch (e: Exception) {
                Log.e("ImageCopy", "Errore nel copiare l'immagine: ${e.message}", e)
                Toast.makeText(currentContext, "Errore nel caricare l'immagine", Toast.LENGTH_SHORT).show()
                selectedImageUri = null
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            if (shouldShowRequestPermissionRationale(currentContext as Activity, Manifest.permission.READ_MEDIA_IMAGES)) {
                showPermissionRationale = true
            } else {
                permissionDeniedPermanently = true
                Toast.makeText(currentContext, "Permesso necessario per selezionare una foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    ScreenTemplate(
        title = "Crea un nuovo profilo",
        currentRoute = Route.Register,
        navController,
        showBottomBar = false
    ) {
        item {
            Image(
                painter = selectedImageUri?.let {
                    rememberAsyncImagePainter(it)
                } ?: painterResource(id = R.drawable.default_profile),
                contentDescription = "Immagine profilo",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
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
                        dobState.value == "Seleziona la data"
                    ) {
                        registrationError = true
                    } else {
                        val email = emailState.value.text
                        val password = passwordState.value.text

                        if (password.length < 6) {
                            Toast.makeText(currentContext, "La password deve contenere almeno 6 caratteri", Toast.LENGTH_SHORT).show()
                            return@CustomButton
                        }

                        authViewModel.registerUser(
                            username = usernameState.value.text,
                            password = password,
                            name = nameState.value.text,
                            email = email,
                            dob = dobState.value,
                            profilePic = selectedImageUri.toString()
                        )
                        navController.navigateSingleTop(Route.Login)
                    }
                },
                text = "Registrati"
            )
        }
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Permesso necessario") },
            text = { Text("Per selezionare un'immagine del profilo, è necessario concedere l'accesso alla galleria.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionRationale = false
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }) {
                    Text("Concedi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Annulla")
                }
            }
        )
    }

    if (permissionDeniedPermanently) {
        AlertDialog(
            onDismissRequest = { permissionDeniedPermanently = false },
            title = { Text("Permesso negato permanentemente") },
            text = { Text("Se desideri selezionare un'immagine del profilo, abilita il permesso nelle impostazioni.") },
            confirmButton = {
                TextButton(onClick = { permissionDeniedPermanently = false }) {
                    Text("OK")
                }
            }
        )
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
