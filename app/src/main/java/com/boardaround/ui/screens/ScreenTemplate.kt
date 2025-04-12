package com.boardaround.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.CustomTopAppBar
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ScreenTemplate(
    title: String,
    currentRoute: Route,
    navController: NavController,
    userViewModel: UserViewModel? = null,
    showBottomBar: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures {
                focusManager.clearFocus()
            }
        }
    ){
        Scaffold(
            topBar = { CustomTopAppBar(title, navController, userViewModel, currentRoute) },
            containerColor = MaterialTheme.colorScheme.background
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                content(PaddingValues(10.dp))
            }
        }

        if (showBottomBar) {
            BottomBar(navController)
        }
    }
}