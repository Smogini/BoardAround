package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.viewmodel.ViewModelFactory

@Composable
fun ShowNewPostScreen(navController: NavController) {

    val context = LocalContext.current
    val postViewModel = remember { ViewModelFactory(context).providePostViewModel() }


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
                    postViewModel.insertPost(
                        title = title.text,
                        content = content.text,
                        imageUri = selectedImageUri?.toString()
                    )
                    navController.popBackStack()
                },
                text = "Pubblica"
            )
        }
    }
}
