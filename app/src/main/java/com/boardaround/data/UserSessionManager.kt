package com.boardaround.data

import android.content.Context
import android.content.SharedPreferences
import com.boardaround.data.entities.User
import com.google.gson.Gson

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val keyUserJson = "user_json"
    private val isUserLogged = "is_logged"

    fun setUserLoggedIn(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString(keyUserJson, userJson).apply()
        prefs.edit().putBoolean(isUserLogged, true).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(isUserLogged, false)
    }

    fun getCurrentUser(): User? {
        val userJson = prefs.getString(keyUserJson, null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    fun logout() {
        prefs.edit().remove(keyUserJson)
                    .putBoolean(isUserLogged, false).apply()
    }

}
