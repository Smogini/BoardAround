package com.boardaround.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CustomSearchBar(
    searchQuery: MutableState<TextFieldValue>,
    placeholderText: String = "Cerca...",
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { newQuery ->
            searchQuery.value = newQuery
            onQueryChange(newQuery.text)
        },
        label = { Text(placeholderText) },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
    )
}
