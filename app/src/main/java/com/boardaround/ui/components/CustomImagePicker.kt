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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.boardaround.R
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Composable
fun CustomImagePicker(
    onImageSelected: (String) -> Unit,
    imageContentDescription: String,
    imageSize: Int = 80
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { it ->
            selectedImageUri = it

            val savedPath = try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val fileName = "profile_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                val outputStream: OutputStream = file.outputStream()

                inputStream?.copyTo(outputStream)

                inputStream?.close()
                outputStream.close()

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            savedPath?.let { onImageSelected(it) }
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
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
    )
}
