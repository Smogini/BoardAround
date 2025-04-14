package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowEventInfoScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    eventViewModel: EventViewModel
) {
    val eventToShow by eventViewModel.selectedEvent.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val currentContext = LocalContext.current

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
        title = eventToShow?.name ?: "EventInfo",
        currentRoute = Route.EventInfo,
        navController = navController,
        userViewModel = userViewModel,
    ) {
        LazyColumn {
            item {
                Text(
                    text = "Descrizione: " + eventToShow?.description,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Indirizzo: " + eventToShow?.address,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Data e ora: " + eventToShow?.dateTime,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Evento privato? " + eventToShow?.isPrivate,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                Image(
                    painter = eventToShow?.imageUrl?.let {
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
            }
        }
    }
}
