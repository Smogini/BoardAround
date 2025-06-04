package com.boardaround.data

import android.content.Context
import android.content.SharedPreferences
import com.boardaround.data.entities.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val keyUserJson = "user_json"

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        val userJson = prefs.getString(keyUserJson, null)
        _currentUser.value = userJson?.let {
            gson.fromJson(it, User::class.java)
        }
    }

    fun setUserLoggedIn(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit()
            .putString(keyUserJson, userJson)
            .apply()
        _currentUser.value = user
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains(keyUserJson)
    }

    fun logout() {
        prefs.edit().remove(keyUserJson).apply()
        _currentUser.value = null
    }

}
