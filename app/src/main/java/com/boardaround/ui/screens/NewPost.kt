package com.boardaround.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomImagePicker
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.viewmodel.PostViewModel

@Composable
fun ShowNewPostScreen(
    navController: NavController,
    postViewModel: PostViewModel
) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    ScreenTemplate(
        title = "New Post",
        currentRoute = Route.NewPost,
        navController = navController,
        showBottomBar = false
    ) {
        item {
            CustomTextField(
                label = "Title",
                value = title,
                onValueChange = { title = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                label = "Description",
                value = content,
                onValueChange = { content = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomTitle(text = "Select an image")
            CustomImagePicker(
                onImageSelected = { path ->
                    selectedImageUri = Uri.parse(path)
                },
                imageContentDescription = "User profile picture"
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                onClick = {
                    postViewModel.insertPost(
                        title = title.text,
                        content = content.text,
                        imageUri = selectedImageUri?.toString()
                    )
                    navController.popBackStack()
                },
                text = "Post"
            )
        }
    }
}
