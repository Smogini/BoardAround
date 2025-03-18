package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.boardaround.ui.screens.LoginScreen
import com.boardaround.ui.theme.BoardAroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme {
                // Qui chiamiamo la funzione NavGraph per gestire la navigazione
//                Surface(color = MaterialTheme.colorScheme.background) {
//                    NavGraph() // Questo è il punto dove la NavGraph viene effettivamente usata
//                }
                LoginScreen()
            }
        }
    }
}
