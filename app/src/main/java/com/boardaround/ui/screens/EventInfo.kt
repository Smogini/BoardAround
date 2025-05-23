package com.boardaround.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
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
                            ).apply {
                                setPackage("com.google.android.apps.maps")
                            }

                            try {
                                currentContext.startActivity(mapIntent)
                            } catch (e: Exception) {
                                Toast.makeText(currentContext, "Impossibile aprire Google Maps", Toast.LENGTH_SHORT).show()
                            }
                        },
                        text = "Apri sulla mappa"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                EventDetailText("Data e ora", eventToShow?.dateTime)
                EventDetailText("Privato", if (eventToShow?.isPrivate == true) "Sì" else "No")

                Spacer(modifier = Modifier.height(24.dp))

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
                        .size(140.dp)
                        .clickable { permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) }
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

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
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = value ?: "-",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

