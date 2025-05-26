package com.boardaround

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.boardaround.data.preferences.AppPreferences
import com.boardaround.navigation.NavGraph
import com.boardaround.ui.theme.BoardAroundTheme
import com.boardaround.viewmodel.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.analytics
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.analytics.logEvent("test_event", null)

        val viewModelFactory = ViewModelFactory(this)
        val userViewModel = viewModelFactory.provideUserViewModel()
        val authViewModel = viewModelFactory.provideAuthViewModel()
        val gameViewModel = viewModelFactory.provideGameViewModel()
        val eventViewModel = viewModelFactory.provideEventViewModel()
        val postViewModel = viewModelFactory.providePostViewModel()
        val triviaViewModel = viewModelFactory.provideTriviaViewModel()

        setContent {
            val context = LocalContext.current
            val appPreferences = remember { AppPreferences(context) }
            val isSystemDark = isSystemInDarkTheme()
            val isDarkMode = remember { mutableStateOf(isSystemDark) }

            /* TODO: non inizializza correttamente la tabella nel db */
            LaunchedEffect(Unit) {
                appPreferences.isDarkMode.collectLatest { isDark ->
                    isDarkMode.value = isDark
                }
                try {
                    viewModelFactory.initializeAchievementManager()
                } catch(e: Exception) {
                    Log.e("main activity", "ERROR: ${e.message}", e)
                }
            }

            BoardAroundTheme(isDarkMode = isDarkMode.value) {
                SystemUiController(isDarkMode.value)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        context = context,
                        navController = navController,
                        userViewModel = userViewModel,
                        authViewModel = authViewModel,
                        gameViewModel = gameViewModel,
                        postViewModel = postViewModel,
                        eventViewModel = eventViewModel,
                        triviaViewModel = triviaViewModel,
                        onThemeChange = { newIsDarkMode ->
                            isDarkMode.value = newIsDarkMode
                            lifecycleScope.launch {
                                appPreferences.setDarkMode(newIsDarkMode)
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
    val view = LocalView.current
    val window = (view.context as Activity).window

    val statusBarColor = if (isDarkMode) {
        Color.Black
    } else {
        Color(0xFFEDE0D4)
    }

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        @Suppress("DEPRECATION")
        window.statusBarColor = statusBarColor.toArgb()

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !isDarkMode
        }
    }
}
