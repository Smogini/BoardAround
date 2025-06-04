package com.boardaround.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomSwitch
import com.boardaround.ui.components.ExpandableSection
import com.boardaround.ui.theme.LocalIsDarkMode
import com.boardaround.viewmodel.AuthViewModel

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun ShowEditMyProfile(
    context: Context,
    navController: NavController,
    authViewModel: AuthViewModel,
    onThemeChange: (Boolean) -> Unit
) {
    val isDarkMode = LocalIsDarkMode.current

    var currentPermission by remember { mutableStateOf<String?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionList = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )

    var confirmAction by remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        permissionList.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    } else {
        permissionList.add(Manifest.permission.READ_MEDIA_IMAGES)
    }

    CustomAlertDialog(
        isVisible = confirmAction,
        title = "Delete account",
        description =
            "Are you sure you want to delete the profile? Once confirmed, you can no longer go back",
        onConfirm = {
            confirmAction = false
            authViewModel.deleteCurrentUser()
        },
        onDismiss = { confirmAction = false }
    )

    ScreenTemplate(
        title = "Edit profile",
        currentRoute = Route.EditMyProfile,
        navController = navController,
        showBottomBar = false
    ) {

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Dark theme")
                CustomSwitch(
                    checked = isDarkMode,
                    onCheckedChange = { onThemeChange(it) }
                )
            }

            ExpandableSection(
                title = "Device permissions",
                icon = Icons.Default.Lock,
                itemList = permissionList,
                labelProvider = { it.replace("android.permission.", "") },
                onItemClick = { currentPermission = it },
                trailingIcon = {
                    CustomClickableIcon(
                        title = "Request",
                        icon = Icons.Default.RequestPage,
                        onClick = {
                            if (isPermissionGranted(context, it)) {
                                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
                            } else {
                                permissionLauncher.launch(it)
                            }
                        }
                    )
                }
            )

            CustomButton(
                onClick = {
//                    pickContactLauncher.launch(null)
                },
                text = "Invite friends to BoardAround"
            )

            CustomButton(
                onClick = { confirmAction = true },
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
