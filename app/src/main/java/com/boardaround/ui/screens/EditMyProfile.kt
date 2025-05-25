package com.boardaround.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomSwitch
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

    var cameraPermissionGranted by remember { mutableStateOf(true)}
    var contactsPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) }
    var locationPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) }
    var notificationsPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) }
    var photosPermissionGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) }

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
        title = "Edit profile",
        currentRoute = Route.EditMyProfile,
        navController = navController
    ) {

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Dark theme")
                CustomSwitch(
                    checked = isDarkMode,
                    onCheckedChange = { newIsDarkMode ->
                        onThemeChange(newIsDarkMode)
                    }
                )
            }

            Text("Device permissions")

            PermissionRow("Camera", cameraPermissionGranted)
            PermissionRow("Contacts", contactsPermissionGranted)
            PermissionRow("Position", locationPermissionGranted)
            PermissionRow("Notifications", notificationsPermissionGranted)
            PermissionRow("Gallery", photosPermissionGranted)

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
                Text("Request authorization")
            }

            if (!cameraPermissionGranted ||
                !contactsPermissionGranted ||
                !locationPermissionGranted ||
                !notificationsPermissionGranted ||
                !photosPermissionGranted
            ) {
                Text(
                    text =
                    "Some authorizations have been denied. You can manually edit them in the app settings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open the app settings")
                }
            }

            Button(
                onClick = {
                    pickContactLauncher.launch(null)
                }
            ) {
                Text("Invite friends to BoardAround")
            }

            CustomButton(
                onClick = { /* TODO: delete account */ },
                text = "Delete account"
            )
        }
    }
}

fun sendInviteMessage(context: Context, phoneNumber: String) {
    val message = "Join BoardAround!"
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
        Text(if (isGranted) "Granted" else "Denied")
    }
}