package com.boardaround.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea un'istanza di DataStore a livello di file o modulo per tutte le preferenze dell'app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val context: Context) {

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        val DARK_MODE = booleanPreferencesKey("dark_mode") // Chiave per il tema scuro
    }

    // --- Gestione Token FCM ---

    // Salva il token FCM
    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN] = token
        }
    }

    // Leggi il token FCM
    val fcmToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN]
        }

    // Cancella il token FCM (utile al logout)
    suspend fun clearFcmToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.FCM_TOKEN)
        }
    }

    // --- Gestione Tema Scuro ---

    // Leggi lo stato del tema scuro
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_MODE] ?: false // Valore predefinito: tema chiaro
        }

    // Salva lo stato del tema scuro
    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = isDarkMode
        }
    }
}