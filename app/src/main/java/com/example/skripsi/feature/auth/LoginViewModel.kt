package com.example.skripsi.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository,
    private val session: SessionManager
) : ViewModel() {

    fun seed() { viewModelScope.launch { repo.registerIfEmpty() } }

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val u = repo.login(username, password)
            if (u == null) {
                launch(Dispatchers.Main) { onError("Username/password salah") }
            } else {
                session.saveUser(u.username, u.role)
                launch(Dispatchers.Main) { onSuccess() }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(ctx: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(ctx.applicationContext)
    private val repo = AuthRepository(db.userDao())
    private val session = SessionManager(ctx.applicationContext)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repo, session) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
