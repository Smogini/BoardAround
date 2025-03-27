package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

class Profile(private val navController: NavController) {

    @Composable
    fun ShowProfileScreen(){

        ScreenTemplate(
            title = "Profilo di :",
            navController,
            showBottomBar = true,
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

            }

        }
    }
}