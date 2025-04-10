package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.Customswitch
import com.boardaround.ui.theme.LocalIsDarkMode

@Composable
fun ShowEditMyProfile(
    navController: NavController,
    onThemeChange: (Boolean) -> Unit // <--- aggiunto
) {
    val isDarkMode = LocalIsDarkMode.current // Ottiene il tema dal CompositionLocal

    ScreenTemplate(
        title = "Modifica profilo",
        currentRoute = Route.EditMyProfile,
        navController = navController
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tema scuro")
                Customswitch(
                    checked = isDarkMode,
                    onCheckedChange = { newIsDarkMode ->
                        onThemeChange(newIsDarkMode) // <--- chiami la funzione
                    }
                )
            }
        }
    }
}
