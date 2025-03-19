package com.boardaround.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boardaround.ui.theme.Background

@Composable
fun ScreenTemplate(
    title: String,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { AppBar(title) },
        bottomBar = { bottomBar?.invoke() },
        containerColor = Background
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            content(contentPadding)
        }
    }
}
