package com.boardaround.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.boardaround.data.entities.User
import com.google.gson.Gson

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_JSON = "user_data"
    }

    fun setUserLoggedIn(user: User, isLoggedIn: Boolean) {
        val userJson = gson.toJson(user)
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.putString(KEY_USER_JSON, userJson)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getCurrentUser(): User? {
        val userJson = prefs.getString(KEY_USER_JSON, null)
        return userJson?.let {
            try {
                gson.fromJson(it, User::class.java)
            } catch (e: Exception) {
                Log.e("UserSessionManager", "Errore nel parsing dell'utente: ${e.message}")
                null
            }
        }
    }

    fun logout() {
        editor.remove(KEY_IS_LOGGED_IN)
        editor.remove(KEY_USER_JSON)
        editor.apply()
    }
}
