package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.boardaround.ui.theme.PrimaryText

class Map(private val navController: NavController) {
    @Composable
    fun ShowMapScreen(){

        ScreenTemplate(
            title = "Mappa",
            navController = navController,
            showBottomBar = true,
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Mappa", textAlign = TextAlign.Center, color = PrimaryText)
            }
        }
    }
}