package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.data.entities.SavedGame
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.network.StreetMapApiResponse
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomCarousel
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomMapField
import com.boardaround.ui.components.CustomSwitch
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.ui.components.DateTimePicker
import com.boardaround.ui.theme.Errors
import com.boardaround.viewmodel.EventViewModel
import com.boardaround.viewmodel.GameViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowNewEventScreen(
    context: Context,
    navController: NavController,
    eventViewModel: EventViewModel,
    gameViewModel: GameViewModel,
    userViewModel: UserViewModel
) {
    var eventNameState by remember { mutableStateOf(TextFieldValue()) }
    var descriptionState by remember { mutableStateOf(TextFieldValue()) }
    var isPrivateEvent by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf<SavedGame?>(null) }

    var selectedDateTime by remember { mutableStateOf("Select date and time") }
    var showDateTimePicker by remember { mutableStateOf(false) }

    val username = userViewModel.getUsername()
    val userGames by gameViewModel.userGames.collectAsState(initial = emptyList())

    var addressState by remember { mutableStateOf(TextFieldValue()) }
    var selectedAddress by remember { mutableStateOf<StreetMapApiResponse?>(null) }
    val suggestedAddresses by eventViewModel.addressSuggestions.collectAsState()

    val eventErrorMessage by eventViewModel.errorMessage.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var cancelAction by remember { mutableStateOf(false) }

    gameViewModel.getUserGames()

    LaunchedEffect(eventErrorMessage) {
       showErrorDialog = eventErrorMessage.isNotBlank()
    }

    ScreenTemplate(
        title = "Create new event",
        currentRoute = Route.NewEvent,
        navController,
    ) {
        item {
            if (cancelAction) {
                CustomAlertDialog(
                    title = "Confirm action",
                    description = "Are you sure you want to cancel?",
                    onConfirm = {
                        cancelAction = false
                        Toast.makeText(context, "Event successfully canceled", Toast.LENGTH_SHORT).show()
                        navController.navigateSingleTop(Route.Homepage)
                    },
                    onDismiss = { cancelAction = false }
                )
            }

            if (showErrorDialog) {
                CustomAlertDialog(
                    title = "Error",
                    description = eventErrorMessage,
                    onDismiss = {
                        showErrorDialog = false
                        eventViewModel.clearErrorMessage()
                    }
                )
            }

            CustomTitle(text = "Enter the name of the event")
            CustomTextField(
                label = "Event name",
                value = eventNameState,
                onValueChange = { eventNameState = it }
            )

            CustomTitle(text = "Enter the description")
            CustomTextField(
                label = "Description",
                value = descriptionState,
                onValueChange = { descriptionState = it }
            )

            CustomTitle(text = "Select event date and time")
            CustomButton(
                onClick = { showDateTimePicker = true },
                text = selectedDateTime
            )

            DateTimePicker(
                isVisible = showDateTimePicker,
                showTimePicker = true,
                onResult = { formattedDate ->
                    selectedDateTime = formattedDate
                },
                onDismiss = { showDateTimePicker = false }
            )

            CustomTitle(text = "Enter event address")
            CustomMapField(
                value = addressState,
                onValueChange = {
                    addressState = it
                    eventViewModel.fetchAddressSuggestions(it.text)
                },
                label = "Address",
                suggestions = suggestedAddresses,
                onSuggestionClick = {
                    selectedAddress = it
                    addressState = TextFieldValue(it.displayName.toString())
                },
                trailingIcon = {
                    if (addressState.text.isNotEmpty()) {
                        CustomClickableIcon(
                            title = "Clear",
                            icon = Icons.Default.Clear,
                            iconColor = Errors,
                            onClick = { addressState = TextFieldValue("") }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            CustomCarousel(
                title = "Select a game for the event",
                titleStyle = MaterialTheme.typography.titleSmall,
                items = userGames,
                onClick = { selectedGame = it },
                imageUrlProvider = { it.imageUrl },
                labelProvider = { it.name }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTitle(text = "Private event")
                CustomSwitch(
                    checked = isPrivateEvent,
                    onCheckedChange = { isPrivateEvent = it }
                )
            }

            CustomButton(
                onClick = {
                    eventViewModel.createEvent(
                        name = eventNameState.text,
                        author = username,
                        description = descriptionState.text,
                        address = selectedAddress?.displayName.toString(),
                        dateTime = selectedDateTime,
                        isPrivate = isPrivateEvent,
                        imageUrl = "No image",
                        gameToPlay = selectedGame?.name.toString()
                    ) {
                        navController.navigateSingleTop(Route.Homepage)
                    }
                },
                text = "Create event"
            )

            CustomButton(
                onClick = { cancelAction = true },
                text = "Cancel"
            )
        }
    }
}
