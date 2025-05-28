package com.boardaround.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomImageCard
import com.boardaround.ui.components.CustomTitle
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowEventInfoScreen(
    context: Context,
    navController: NavController,
    userViewModel: UserViewModel,
    eventViewModel: EventViewModel
) {
    val eventToShow by eventViewModel.selectedEvent.collectAsState()
    val dateTime = eventToShow?.dateTime?.replace(" ", ", ")

    ScreenTemplate(
        title = eventToShow?.name ?: "Event's details",
        currentRoute = Route.EventInfo,
        navController = navController,
        userViewModel = userViewModel,
    ) {

        item {
            CustomImageCard(
                item = null,
                onClick = {},
                image = eventToShow?.imageUrl.toString(),
                cardSize = 150
            )

            EventDetailText("Name of the event", eventToShow?.name)
            EventDetailText("Description", eventToShow?.description)
            EventDetailText("Where it will be held", eventToShow?.address)

            eventToShow?.address?.let { address ->
                Spacer(modifier = Modifier.height(8.dp))
                CustomButton(
                    onClick = {
                        val mapIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}")
                        ).apply {
                            setPackage("com.google.android.apps.maps")
                        }

                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Unable to open Google Maps", Toast.LENGTH_SHORT).show()
                        }
                    },
                    text = "Open on the map"
                )
            }

            EventDetailText("Will be held on the day, at hours:", dateTime)
            EventDetailText("Is it private?", eventToShow?.isPrivate.toString())

            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(
                onClick = {
                    // TODO: Azione per partecipare
                },
                text = "Participate in '${eventToShow?.name.toString()}'"
            )
        }
    }
}

@Composable
private fun EventDetailText(label: String, value: String? = "No value") {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        CustomTitle(
            text = label,
            alignment = TextAlign.Start,
            textStyle = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
