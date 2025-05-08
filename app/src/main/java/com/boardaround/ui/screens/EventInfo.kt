package com.boardaround.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import android.content.Intent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
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
    ) { isGranted ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(
                currentContext,
                "Permesso necessario per selezionare una foto",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    ScreenTemplate(
        title = eventToShow?.name ?: "Dettagli Evento",
        currentRoute = Route.EventInfo,
        navController = navController,
        userViewModel = userViewModel,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                EventDetailText("Nome Evento", eventToShow?.name)
                EventDetailText("Descrizione", eventToShow?.description)


                EventDetailText("Indirizzo", eventToShow?.address)

                eventToShow?.address?.let { address ->
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomButton(
                        onClick = {
                            val mapIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}")
                            )
                            mapIntent.setPackage("com.google.android.apps.maps")

                            try {
                                currentContext.startActivity(mapIntent)
                            } catch (e: Exception) {
                                Toast.makeText(currentContext, "Impossibile aprire Google Maps", Toast.LENGTH_SHORT).show()
                            }
                        },
                        text = "Apri sulla mappa"
                    )
                }


                EventDetailText("Data e ora", eventToShow?.dateTime)
                EventDetailText("Privato", if (eventToShow?.isPrivate == true) "Sì" else "No")

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = eventToShow?.imageUrl?.let {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(currentContext)
                                .data(it)
                                .crossfade(true)
                                .build()
                        )
                    } ?: painterResource(id = R.drawable.default_profile),
                    contentDescription = "Immagine Evento",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }

                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    onClick = {
                        // TODO: Azione per partecipare
                    },
                    text = "Partecipa all'evento"
                )
            }
        }
    }
}

@Composable
fun EventDetailText(label: String, value: String?) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value ?: "-",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
