package com.boardaround.ui.screens

//Se volessimo implementare uno stile import com.boardaround.searchbar.ui.theme.SearchBarTheme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.ui.components.CustomSearchBar
import com.boardaround.viewmodel.UserViewModel

class Homepage(private val navController: NavController): ComponentActivity() {

    @Composable
    fun ShowHomePageScreen(userViewModel: UserViewModel) {
        ScreenTemplate(
            title = "Homepage",
            showBottomBar = true,
            navController = navController,
        ) {
            val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

            Column (
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomSearchBar(searchQuery = searchQuery,
                    onQueryChange = { query ->
                        // Gestire il cambiamento della query (esempio: filtrare i dati)
                        println("Query cambiata: $query")
                    }
                )

            }

        }
    }

    @Composable
    fun SearchBar(searchQuery: MutableState<TextFieldValue>) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { newQuery ->
                searchQuery.value = newQuery
            },
            label = { Text("Cerca...") },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            singleLine = true
        )
    }

}