package com.boardaround.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.Customswitch
import com.boardaround.ui.theme.LocalIsDarkMode

@Composable
fun ShowEditMyProfile(
    navController: NavController,
    onThemeChange: (Boolean) -> Unit
) {
    val isDarkMode = LocalIsDarkMode.current
    val context = LocalContext.current

    val pickContactLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let { contactUri ->
            val cursor = context.contentResolver.query(
                contactUri,
                null,
                null,
                null,
                null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    if (numberIndex >= 0) {
                        val phoneNumber = it.getString(numberIndex)
                        sendInviteMessage(context, phoneNumber)
                    }
                }
            }
        }
    }

    // Stati per le autorizzazioni
    var cameraPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    var contactsPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) }
    var locationPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) }
    var notificationsPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) }
    var photosPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) }

    // Launcher per richiedere le autorizzazioni
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: cameraPermissionGranted
        contactsPermissionGranted = permissions[Manifest.permission.READ_CONTACTS] ?: contactsPermissionGranted
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: locationPermissionGranted
        notificationsPermissionGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] ?: notificationsPermissionGranted
        photosPermissionGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: photosPermissionGranted
    }

    ScreenTemplate(
        title = "Modifica profilo",
        currentRoute = Route.EditMyProfile,
        navController = navController
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tema scuro")
                Customswitch(
                    checked = isDarkMode,
                    onCheckedChange = { newIsDarkMode ->
                        onThemeChange(newIsDarkMode)
                    }
                )
            }

            Text("Autorizzazioni del dispositivo")

            PermissionRow("Fotocamera", cameraPermissionGranted)
            PermissionRow("Contatti", contactsPermissionGranted)
            PermissionRow("Posizione", locationPermissionGranted)
            PermissionRow("Notifiche", notificationsPermissionGranted)
            PermissionRow("Foto", photosPermissionGranted)

            Button(onClick = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            }) {
                Text("Richiedi autorizzazioni")
            }


            Button(
                onClick = {
                    pickContactLauncher.launch(null)
                }
            ) {
                Text("Invita amici su BoardAround")
            }
        }

    }
}

fun sendInviteMessage(context: android.content.Context, phoneNumber: String) {
    val message = "Unisciti a BoardAround!"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra("address", phoneNumber)
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
fun PermissionRow(permissionName: String, isGranted: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(permissionName)
        Text(if (isGranted) "Concessa" else "Negata")
    }
}