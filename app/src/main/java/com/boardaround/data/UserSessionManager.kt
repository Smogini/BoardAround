package com.boardaround.data

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private var username: String = ""

    private val isLoggedIn = "is_logged_in"

    fun setUserLoggedIn(isLoggedIn: Boolean, username: String) {
        this.username = username
        editor.putBoolean(this.isLoggedIn, isLoggedIn)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(isLoggedIn, false)
    }

    fun logout() {
        editor.putBoolean(isLoggedIn, false)
        editor.apply()
    }

    fun getUsername(): String {
        return username
    }
}
