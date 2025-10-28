package com.example.skripsi.core.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(ctx: Context) {
    private val pref: SharedPreferences = ctx.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(username: String, role: String) {
        pref.edit().putString("username", username).putString("role", role).apply()
    }

    fun clear() { pref.edit().clear().apply() }

    val username: String? get() = pref.getString("username", null)
    val role: String? get() = pref.getString("role", null)
    val isLoggedIn: Boolean get() = username != null && role != null
    val isAdmin: Boolean get() = role == "ADMIN"
}
