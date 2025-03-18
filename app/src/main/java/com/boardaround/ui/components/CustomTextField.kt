package com.boardaround.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.boardaround.ui.theme.Background
import com.boardaround.ui.theme.PrimaryText

@Composable
fun CustomTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = PrimaryText,
            unfocusedTextColor = PrimaryText,
            focusedContainerColor = Background,
            unfocusedContainerColor = Background,
            cursorColor = PrimaryText,
            focusedIndicatorColor = PrimaryText,
            unfocusedIndicatorColor = PrimaryText,
            focusedLabelColor = PrimaryText,
            unfocusedLabelColor = PrimaryText,
        ),
        visualTransformation = if (label == "Password") PasswordVisualTransformation() else VisualTransformation.None
    )
}
