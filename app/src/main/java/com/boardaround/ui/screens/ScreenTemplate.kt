package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.CustomTopAppBar
import com.boardaround.ui.theme.Background

@Composable
fun ScreenTemplate(
    title: String,
    navController: NavController,
    showBottomBar: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Scaffold(
            topBar = { CustomTopAppBar(title, navController) },
            containerColor = Background
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