package com.example.skripsi.core.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    val isLoggedIn: Boolean
        get() = prefs.getBoolean("is_logged_in", false)

    val userId: Int
        get() = prefs.getInt("user_id", -1)

    val userName: String?
        get() = prefs.getString("user_name", null)

    val userRole: String?
        get() = prefs.getString("user_role", null)

    fun login(userId: Int, userName: String, role: String) {
        prefs.edit().apply {
            putBoolean("is_logged_in", true)
            putInt("user_id", userId)
            putString("user_name", userName)
            putString("user_role", role)
            apply()
        }
    }

    // Tambahkan fungsi saveUser
    fun saveUser(userName: String, role: String) {
        prefs.edit().apply {
            putBoolean("is_logged_in", true)
            putString("user_name", userName)
            putString("user_role", role)
            apply()
        }
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
