package com.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.utils.PreferencesManager
import com.boardaround.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactory(this)
        val postViewModel = viewModelFactory.providePostViewModel()
        val userViewModel = viewModelFactory.provideUserViewModel()
        val authViewModel = viewModelFactory.provideAuthViewModel()
        val gameViewModel = viewModelFactory.provideGameViewModel()

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
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController,
                        userViewModel,
                        authViewModel,
                        postViewModel,
                        gameViewModel,
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