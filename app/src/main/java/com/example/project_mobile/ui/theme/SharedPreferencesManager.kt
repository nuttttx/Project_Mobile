package com.example.project_mobile.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(private val context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userEmail: String?
        get() = preferences.getString(KEY_USER_EMAIL, null)
        set(value) = preferences.edit().putString(KEY_USER_EMAIL, value).apply()


    var userId: Int?
        get() = preferences.getInt(KEY_USER_ID, -1).takeIf { it != -1 }
        set(value) = preferences.edit().putInt(KEY_USER_ID, value ?: -1).apply()

    var userName: String?
    get() = preferences.getString(KEY_USER_NAME, null)
    set(value) = preferences.edit().putString(KEY_USER_NAME, value).apply()


    fun clearUserAll() {
        preferences.edit {
            remove(KEY_USER_EMAIL)
            remove(KEY_IS_LOGGED_IN)
        }
    }

    fun clearUserLogin() {
        preferences.edit {
            remove(KEY_IS_LOGGED_IN)
        }
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"


    }
}
