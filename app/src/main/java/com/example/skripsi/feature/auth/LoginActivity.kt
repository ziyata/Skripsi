package com.example.skripsi.feature.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.R
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.feature.auth.vm.LoginViewModel
import com.example.skripsi.feature.auth.vm.LoginViewModelFactory
import com.example.skripsi.feature.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var vm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        if (session.isLoggedIn) {
            goMain()
            return
        }

        setContentView(R.layout.activity_login)
        vm = ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]
        vm.seed()

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btn = findViewById<Button>(R.id.btnLogin)

        btn.setOnClickListener {
            val u = etUser.text.toString().trim()
            val p = etPass.text.toString()
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Lengkapi username/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.login(u, p,
                onSuccess = { goMain() },
                onError = { msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
            )
        }
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
