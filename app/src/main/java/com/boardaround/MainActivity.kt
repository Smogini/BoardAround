package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactory(this)

        setContent {
            BoardAroundTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val userViewModel = viewModelFactory.provideUserViewModel()
                    val authViewModel = viewModelFactory.provideAuthViewModel()
                    NavGraph(navController, userViewModel, authViewModel)
                }
            }
        }
    }
}
