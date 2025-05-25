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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val context: Context) {

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN] = token
        }
    }

    val fcmToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN]
        }

    suspend fun clearFcmToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.FCM_TOKEN)
        }
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_MODE] ?: false
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = isDarkMode
        }
    }
}