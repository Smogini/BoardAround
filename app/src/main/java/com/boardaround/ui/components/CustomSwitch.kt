package com.boardaround.ui.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.boardaround.ui.theme.ButtonColor
import com.boardaround.ui.theme.PrimaryText

@Composable
fun Customswitch() {
    var checked by remember { mutableStateOf(false) }

    Switch(
        checked = checked,
        onCheckedChange = { checked = it},
        colors = SwitchDefaults.colors(
            checkedThumbColor = PrimaryText,
            checkedTrackColor = ButtonColor,
            uncheckedThumbColor = PrimaryText,
            uncheckedTrackColor = Color.Gray
        )
    )
}