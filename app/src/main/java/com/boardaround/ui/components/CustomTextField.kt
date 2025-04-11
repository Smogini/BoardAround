package com.boardaround.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null, // Aggiunto parametro leadingIcon
    trailingIcon: (@Composable () -> Unit)? = null // Aggiunto parametro trailingIcon
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            leadingIcon = leadingIcon, // Utilizzo del parametro leadingIcon
            trailingIcon = trailingIcon // Utilizzo del parametro trailingIcon
        )
    }
}
