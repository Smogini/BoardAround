package com.boardaround.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boardaround.data.entities.Event


@Composable
fun NoticeBoardSection(
    events: List<Event>,
    onEventClick: ((Event) -> Unit)? = null
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        CustomTitle(text = "Notice Board")

        if (events.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            CustomTitle(text = "No joined events yet.")
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            events.forEach { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick?.invoke(event) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
