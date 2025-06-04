package com.boardaround.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.boardaround.network.StreetMapApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMapField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    suggestions: List<StreetMapApiResponse>,
    onSuggestionClick: (StreetMapApiResponse) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded && suggestions.isNotEmpty(),
        onExpandedChange = {  }
    ) {
        CustomTextField(
            label = label,
            value = value.text,
            onValueChange = {
                isExpanded = it.isNotBlank() && suggestions.isNotEmpty()
                onValueChange(TextFieldValue(it))
            },
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            trailingIcon = trailingIcon
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 4.dp)
        ) {
            suggestions.forEachIndexed { index, suggestion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = suggestion.displayName.orEmpty(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        isExpanded = false
                        onSuggestionClick(suggestion)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp)
                )

                if (index < suggestions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .height(1.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}
