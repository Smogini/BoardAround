package com.boardaround.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isPasswordField: Boolean = false
) {
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (isPasswordField) {
                val icon = if (passwordVisible.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                CustomButtonIcon(
                    title = "Toggle password visibility",
                    icon = icon,
                    iconColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }
                )
            }
            if (trailingIcon != null) {
                trailingIcon()
            }
        },
        visualTransformation = if (isPasswordField && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None
    )
}
