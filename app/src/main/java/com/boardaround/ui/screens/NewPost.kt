package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.viewmodel.PostViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowNewPostScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel
) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImagePermission by remember { mutableStateOf(false) }
    val username = userViewModel.getUsername()

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
    ) {
        item {
            CustomTextField(
                label = "Titolo",
                value = title,
                onValueChange = { title = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                label = "Contenuto",
                value = content,
                onValueChange = { content = it }
            )

            // Bottone per selezionare l'immagine
            CustomButton(
                onClick = {
                    val permissionToRequest =
                        Manifest.permission.READ_MEDIA_IMAGES
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

            Spacer(modifier = Modifier.height(300.dp))

            CustomButton(
                onClick = {
                    postViewModel.insertPost(
                        title = title.text,
                        content = content.text,
                        imageUri = selectedImageUri?.toString(),
                        author = username
                    )
                    navController.popBackStack()
                },
                text = "Pubblica"
            )
        }
    }
}
