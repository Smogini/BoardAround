package com.example.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.boardaround.navigation.NavGraph
import com.example.boardaround.ui.theme.BoardAroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme {
                // Qui chiamiamo la funzione NavGraph per gestire la navigazione
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph() // Questo è il punto dove la NavGraph viene effettivamente usata
                }
            }
        }
    }
}
