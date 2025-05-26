package com.boardaround.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.boardaround.R

@Composable
fun CustomImagePicker(
    onImageSelected: (Uri) -> Unit,
    imageContentDescription: String,
    imageSize: Int = 80
) {
    var showPermissionRationale by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
        }
    }

    Image(
        painter = selectedImageUri?.let {
            rememberAsyncImagePainter(it)
        } ?: painterResource(id = R.drawable.default_profile),
        contentDescription = imageContentDescription,
        modifier = Modifier
            .size(imageSize.dp)
            .clip(CircleShape)
            .clickable {
                permissionLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                selectedImageUri?.let(onImageSelected)
            }
    )

    CustomAlertDialog(
        isVisible = permissionDeniedPermanently,
        title = "Permit permanently denied",
        description =
        "If you want to select a profile picture, enable permission in the settings.",
        onDismiss = { permissionDeniedPermanently = false }
    )

    CustomAlertDialog(
        isVisible = showPermissionRationale,
        title = "Permit needed",
        description =
        "To select a profile picture, you must grant access to the gallery.",
        onDismiss = { showPermissionRationale = false }
    )

}