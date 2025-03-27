package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
    showBottomBar: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { CustomTopAppBar(title, navController) },
        bottomBar = { if (showBottomBar) { BottomBar(navController) } },
        containerColor = Background
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            content(PaddingValues(10.dp))
        }
    }
}
