package com.boardaround.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import com.boardaround.network.NominatimClient
import com.boardaround.network.SearchResult
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun CustomMapField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onSuggestionClick: ((SearchResult) -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null, // Aggiunto parametro leadingIcon
    trailingIcon: (@Composable () -> Unit)? = null // Aggiunto parametro trailingIcon
) {
    var suggestions by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)

                // Se il testo non è vuoto, esegui la ricerca
                if (it.text.isNotEmpty()) {
                    // Chiamata API solo se il testo è valido
                    NominatimClient.instance.search(query = it.text).enqueue(object : retrofit2.Callback<List<SearchResult>> {
                        override fun onResponse(call: retrofit2.Call<List<SearchResult>>, response: retrofit2.Response<List<SearchResult>>) {
                            if (response.isSuccessful) {
                                val responseBody = response.body() ?: emptyList()
                                suggestions = responseBody
                                showSuggestions = suggestions.isNotEmpty()

                                Log.d("CustomMapField", "API Response: $responseBody")
                            } else {
                                // Gestisci errore nella risposta
                                suggestions = emptyList()
                                showSuggestions = false
                                Log.e("CustomMapField", "API Error: ${response.code()} - ${response.message()}")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<List<SearchResult>>, t: Throwable) {
                            // Gestisci il fallimento della chiamata API
                            suggestions = emptyList()
                            showSuggestions = false
                            Log.e("CustomMapField", "API Failure: ${t.message}", t)
                        }
                    })
                } else {
                    // Se il testo è vuoto, non fare chiamate API e nascondi le suggerimenti
                    suggestions = emptyList()
                    showSuggestions = false
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            leadingIcon = leadingIcon, // Utilizzo del parametro leadingIcon
            trailingIcon = trailingIcon // Utilizzo del parametro trailingIcon
        )

        // Mostra i suggerimenti solo se ci sono
        if (showSuggestions && onSuggestionClick != null) {
            Popup(
                alignment = Alignment.BottomStart,
                offset = IntOffset(0, 0),
                properties = PopupProperties(focusable = false)
            ) {
                Surface(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 4.dp),
                    shape = MaterialTheme.shapes.small,
                    tonalElevation = 2.dp
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(suggestions) { suggestion ->
                            Text(
                                text = suggestion.displayName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Quando un suggerimento viene cliccato, invoca la funzione
                                        onSuggestionClick(suggestion)
                                        showSuggestions = false
                                    }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
