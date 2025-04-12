package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.ui.theme.LocalIsDarkMode
import com.boardaround.viewmodel.ViewModelFactory
import com.boardaround.utils.PreferencesManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(this)
        val postViewModel = viewModelFactory.providePostViewModel()

        setContent {
            val context = LocalContext.current
            val preferencesManager = remember { PreferencesManager(context) }
            val isSystemDark = isSystemInDarkTheme()
            val isDarkMode = remember { mutableStateOf(isSystemDark) } // Stato del tema gestito da MainActivity

            // Legge la preferenza all'avvio e la rilegge quando cambia
            LaunchedEffect(Unit) {
                preferencesManager.isDarkMode.collectLatest { isDark ->
                    isDarkMode.value = isDark // Aggiorna lo stato del tema
                }
            }

            BoardAroundTheme(isDarkMode = isDarkMode.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val userViewModel = viewModelFactory.provideUserViewModel()
                    val authViewModel = viewModelFactory.provideAuthViewModel()
                    NavGraph(
                        navController,
                        userViewModel,
                        authViewModel,
                        postViewModel,
                        onThemeChange = { newIsDarkMode ->
                            isDarkMode.value = newIsDarkMode
                            lifecycleScope.launch {
                                preferencesManager.setDarkMode(newIsDarkMode)
                            }
                        }
                    )
                }
            }
        }
    }
}