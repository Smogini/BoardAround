package com.boardaround.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.boardaround.network.NominatimClient
import com.boardaround.network.StreetMapApiResponse

@Composable
fun CustomMapField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onSuggestionClick: ((StreetMapApiResponse) -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    var suggestions by remember { mutableStateOf<List<StreetMapApiResponse>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)

                // Se il testo non è vuoto, esegui la ricerca
                if (it.text.isNotEmpty()) {
                    NominatimClient.instance.search(query = it.text, countryCodes = "it").enqueue(object : retrofit2.Callback<List<StreetMapApiResponse>> {
                        override fun onResponse(call: retrofit2.Call<List<StreetMapApiResponse>>, response: retrofit2.Response<List<StreetMapApiResponse>>) {
                            if (response.isSuccessful) {
                                val responseBody = response.body() ?: emptyList()
                                suggestions = responseBody
                                showSuggestions = suggestions.isNotEmpty()

                                responseBody.forEach { suggestion ->
                                    Log.d("CustomMapField", "Lat: ${suggestion.lat}, Lon: ${suggestion.lon}")
                                }

                                Log.d("CustomMapField", "API Response: $responseBody")
                            } else {
                                suggestions = emptyList()
                                showSuggestions = false
                                Log.e("CustomMapField", "API Error: ${response.code()} - ${response.message()}")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<List<StreetMapApiResponse>>, t: Throwable) {
                            suggestions = emptyList()
                            showSuggestions = false
                            Log.e("CustomMapField", "API Failure: ${t.message}", t)
                        }
                    })
                } else {
                    suggestions = emptyList()
                    showSuggestions = false
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )

        if (showSuggestions && onSuggestionClick != null) {
            Popup(
                alignment = Alignment.BottomEnd,
                offset = IntOffset(0, 50),
                properties = PopupProperties(focusable = false)
            ) {
                Surface(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(top = 4.dp),
                    shape = MaterialTheme.shapes.small,
                    tonalElevation = 2.dp
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(suggestions) { suggestion ->
                            Text(
                                text = suggestion.displayName ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Verifica se la latitudine e longitudine sono disponibili
                                        val lat = suggestion.lat
                                        val lon = suggestion.lon
                                        Log.d("CustomMapField", "Latitudine selezionata: $lat, Longitudine selezionata: $lon")

                                        if (lat != null && lon != null) {
                                            // Passa latitudine e longitudine
                                            onSuggestionClick(suggestion)
                                        } else {
                                            // Se latitudine e longitudine sono nulli, gestisci il caso
                                            Log.e("CustomMapField", "Latitudine e longitudine non disponibili per questo suggerimento")
                                        }
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
