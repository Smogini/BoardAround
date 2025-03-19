package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.boardaround.ui.theme.Background
import com.boardaround.ui.theme.Divider
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.PrimaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = PrimaryText
                )
            },
            actions = {
                if (title.length != 25) { /* TODO da modificare, non deve disegnare l'icona nella schermata di login/registrazione */
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.NotificationsNone, "Empty notifications")
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Background,
                actionIconContentColor = BottomBar
            )
        )
        HorizontalDivider(
            color = Divider,
            thickness = 4.dp
        )
    }
}