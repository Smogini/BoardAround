package com.boardaround.ui.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.boardaround.utils.Game

@Composable
fun MyGamesCarousel(games: List<Game>) { // Accetta 'games' come parametro
    if (games.isNotEmpty()) {
        LazyRow {
            items(games) { game ->
                Text(text = game.name) // Sostituisci con la visualizzazione desiderata
            }
        }
    } else {
        Text("Caricamento giochi...")
    }
}