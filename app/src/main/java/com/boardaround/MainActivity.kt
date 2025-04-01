package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val userViewModel: UserViewModel = viewModel()
                    NavGraph(navController, userViewModel)
                }
            }
        }
    }
}
