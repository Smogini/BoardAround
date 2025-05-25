package com.boardaround.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import coil.compose.rememberAsyncImagePainter
import com.boardaround.R
import java.io.File
import java.io.FileOutputStream

@Composable
fun CustomImagePicker(
    onImageSelected: (Uri) -> Unit,
    context: Context,
    imageContentDescription: String
) {
    var showPermissionRationale by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }
    val selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val fileName = "profile_pic_${System.currentTimeMillis()}.jpg"
            val destinationFile = File(context.filesDir, fileName)

            try {
                context.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                val copiedUri = Uri.fromFile(destinationFile)
                onImageSelected(copiedUri)
            } catch (e: Exception) {
                Log.e("ImagePicker", "Error copying the image: ${e.message}", e)
                Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            if (shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_MEDIA_IMAGES)) {
                showPermissionRationale = true
            } else {
                permissionDeniedPermanently = true
                Toast.makeText(context, "Permit denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Image(
        painter = selectedImageUri?.let {
            rememberAsyncImagePainter(it)
        } ?: painterResource(id = R.drawable.default_profile),
        contentDescription = imageContentDescription,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .clickable {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
    )

    if (permissionDeniedPermanently) {
        CustomAlertDialog(
            title = "Permit permanently denied",
            description =
            "If you want to select a profile picture, enable permission in the settings.",
            onDismiss = { permissionDeniedPermanently = false }
        )
    }

    if (showPermissionRationale) {
        CustomAlertDialog(
            title = "Permit needed",
            description =
            "To select a profile picture, you must grant access to the gallery.",
            onDismiss = { showPermissionRationale = false }
        )
    }

}