package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
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
import com.boardaround.network.StreetMapApiResponse
import com.boardaround.ui.components.CustomAlertDialog
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomCarousel
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomImagePicker
import com.boardaround.ui.components.CustomMapField
import com.boardaround.ui.components.CustomSwitch
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.components.CustomTitle
import com.boardaround.ui.components.DateTimePicker
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
    var selectedImage by remember { mutableStateOf<String?>("No image") }

    var selectedDateTime by remember { mutableStateOf("Select date and time") }
    var showDateTimePicker by remember { mutableStateOf(false) }

    val userGames by gameViewModel.userGames.collectAsState(initial = emptyList())

    var addressState by remember { mutableStateOf(TextFieldValue()) }
    var selectedAddress by remember { mutableStateOf<StreetMapApiResponse?>(null) }
    val suggestedAddresses by eventViewModel.addressSuggestions.collectAsState()

    val eventErrorMessage by eventViewModel.errorMessage.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var cancelAction by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        gameViewModel.getUserGames()
    }

    LaunchedEffect(eventErrorMessage) {
       showErrorDialog = eventErrorMessage.isNotBlank()
    }

    CustomAlertDialog(
        isVisible = cancelAction,
        title = "Confirm action",
        description = "Are you sure you want to cancel?",
        onConfirm = {
            cancelAction = false
            Toast.makeText(context, "Event successfully canceled", Toast.LENGTH_SHORT).show()
            navController.navigate(Route.Homepage)
        },
        onDismiss = { cancelAction = false }
    )

    CustomAlertDialog(
        isVisible = showErrorDialog,
        title = "Error",
        description = eventErrorMessage,
        onDismiss = {
            showErrorDialog = false
            eventViewModel.clearErrorMessage()
        }
    )

    DateTimePicker(
        isVisible = showDateTimePicker,
        showTimePicker = true,
        onResult = { formattedDate ->
            selectedDateTime = formattedDate
        },
        onDismiss = { showDateTimePicker = false }
    )

    ScreenTemplate(
        title = "Create new event",
        currentRoute = Route.NewEvent,
        navController = navController,
        showBottomBar = false
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            CustomImagePicker(
                onImageSelected = { selectedImage = it },
                imageContentDescription = "Enter the image of the event"
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))

            CustomTitle(text = "Enter the name of the event")
            CustomTextField(
                label = "Event name",
                value = eventNameState.text,
                onValueChange = { eventNameState = TextFieldValue(it) }
            )
        }

        item {
            CustomTitle(text = "Enter the description")
            CustomTextField(
                label = "Description",
                value = descriptionState.text,
                onValueChange = { descriptionState = TextFieldValue(it) }
            )
        }

        item {
            CustomTitle(text = "Select event date and time")
            CustomButton(
                onClick = { showDateTimePicker = true },
                text = selectedDateTime,
                leadingIcon = {
                    CustomClickableIcon(
                        title = "Select date and time",
                        icon = Icons.Default.AccessTime,
                    )
                }
            )
        }

        item {
            CustomTitle(text = "Enter event address")
            CustomMapField(
                value = addressState,
                onValueChange = {
                    addressState = it
                    eventViewModel.onQueryChange(it.text)
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
                            iconColor = MaterialTheme.colorScheme.error,
                            onClick = { addressState = TextFieldValue("") }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            CustomCarousel(
                title = "Select a game for the event",
                titleStyle = MaterialTheme.typography.titleSmall,
                items = userGames,
                onClick = { selectedGame = it },
                imageUrlProvider = { it.imageUrl },
                cardShape = CircleShape
            )
        }

        item {
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
        }

        item {
            CustomButton(
                onClick = {
                    val currentUser = userViewModel.currentUser.value
                    val uid = currentUser?.uid .orEmpty()
                    val username = currentUser?.username.orEmpty()

                    eventViewModel.createEvent(
                        name = eventNameState.text,
                        author = username,
                        authorUID = uid,
                        description = descriptionState.text,
                        address = selectedAddress?.displayName.toString(),
                        dateTime = selectedDateTime,
                        isPrivate = isPrivateEvent,
                        imageUrl = selectedImage.toString(),
                        gameToPlay = selectedGame?.name.toString()
                    ) {
                        Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Route.Homepage)
                    }
                },
                text = "Create event"
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(
                onClick = { cancelAction = true },
                text = "Cancel"
            )
        }
    }
}
