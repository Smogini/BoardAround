package com.boardaround.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> ExpandableSection(
    title: String,
    items: List<T>,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    emptyMessage: String = "Nessun elemento disponibile",
    onItemClick: ((T) -> Unit)?,
    itemContent: @Composable (T) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange(!isExpanded) }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = if (isExpanded) "Mostra meno" else "Mostra di più"
            )
        }

        if (isExpanded) {
            if (items.isNotEmpty()) {
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    items(items) { item ->
                        Box(
                            modifier = Modifier.fillMaxWidth().clickable { onItemClick?.let { it(item) } }
                        ) {
                            itemContent(item)
                        }
                    }
                }
            } else {
                Text(
                    text = emptyMessage,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
