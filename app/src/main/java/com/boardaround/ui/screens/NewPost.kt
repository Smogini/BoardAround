package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField

@Composable
fun ShowNewPostScreen(navController: NavController) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImagePermission by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasImagePermission = isGranted
            if (isGranted) {
                imagePickerLauncher.launch("image/*")
            } else {
                // TODO: Gestire il caso in cui il permesso non è concesso (es. mostrare un messaggio all'utente)
                println("Permesso negato")
            }
        }
    )

    ScreenTemplate(
        title = "Nuovo Post",
        currentRoute = Route.NewPost,
        navController = navController,
        showBottomBar = false
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                label = "Titolo",
                value = title,
                onValueChange = { title = it }
            )
            CustomTextField(
                label = "Contenuto",
                value = content,
                onValueChange = { content = it }
            )

            // Bottone per selezionare l'immagine
            CustomButton(
                onClick = {
                    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    if (hasImagePermission) {
                        imagePickerLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                },
                text = "Seleziona Immagine"
            )

            // Visualizzazione dell'immagine selezionata
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Immagine selezionata",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            CustomButton(
                onClick = {
                    // TODO: Implementa la logica per salvare il post con l'immagine
                    println("Titolo: ${title.text}, Contenuto: ${content.text}, Immagine: $selectedImageUri")
                    navController.popBackStack()
                },
                text = "Pubblica"
            )
        }
    }
}