package com.boardaround.ui.screens

//Se volessimo implementare uno stile import com.boardaround.searchbar.ui.theme.SearchBarTheme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButtonIcon
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.theme.Errors
import com.boardaround.ui.theme.PrimaryText
import com.boardaround.viewmodel.UserViewModel

class Homepage(private val navController: NavController): ComponentActivity() {

    @Composable
    fun ShowHomePageScreen(userViewModel: UserViewModel) {
        if (userViewModel.isUserLoggedIn()) {
            val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

            ScreenTemplate(
                title = "Homepage",
                showBottomBar = true,
                navController = navController,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CustomTextField(
                        label = "Cosa stai cercando?",
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        leadingIcon = {
                            CustomButtonIcon(
                                title = "Search",
                                icon = Icons.Filled.Search,
                                iconColor = PrimaryText,
                                onClick = { /* TODO */ }
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.value.text.isNotEmpty()) {
                                CustomButtonIcon(
                                    title = "Clear",
                                    icon = Icons.Filled.Clear,
                                    iconColor = Errors,
                                    onClick = { searchQuery.value = TextFieldValue("") }
                                )
                            }
                        },
                    )

                }
            }
        } else {
            navController.navigate(Route.Login) {
                launchSingleTop = true
            }
        }
    }
}