package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.utils.PreferencesManager
import com.boardaround.viewmodel.ViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController // Importa
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactory(this)
        val userViewModel = viewModelFactory.provideUserViewModel()
        val authViewModel = viewModelFactory.provideAuthViewModel()
        val gameViewModel = viewModelFactory.provideGameViewModel()
        val eventViewModel = viewModelFactory.provideEventViewModel()
        val postViewModel = viewModelFactory.providePostViewModel()

        setContent {
            val context = LocalContext.current
            val preferencesManager = remember { PreferencesManager(context) }
            val isSystemDark = isSystemInDarkTheme()
            val isDarkMode = remember { mutableStateOf(isSystemDark) }

            LaunchedEffect(Unit) {
                preferencesManager.isDarkMode.collectLatest { isDark ->
                    isDarkMode.value = isDark
                }
            }

            BoardAroundTheme(isDarkMode = isDarkMode.value) {
                // Gestione della barra di stato
                SystemUiController(isDarkMode.value)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        userViewModel = userViewModel,
                        authViewModel = authViewModel,
                        gameViewModel = gameViewModel,
                        postViewModel = postViewModel,
                        eventViewModel = eventViewModel,
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

@Composable
fun SystemUiController(isDarkMode: Boolean) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isDarkMode // Determina se usare icone scure o chiare

    val statusBarColor = if (isDarkMode) {
        Color.Black // Colore nero per il tema scuro
    } else {
        Color(0xFFEDE0D4) // Colore per il tema chiaro
    }

    DisposableEffect(systemUiController, useDarkIcons) {
        // Aggiorna il colore della barra di stato
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }
}