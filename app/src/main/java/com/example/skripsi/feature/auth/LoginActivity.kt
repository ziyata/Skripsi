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
import com.example.skripsi.databinding.ActivityLoginBinding
import com.example.skripsi.feature.auth.vm.LoginViewModel
import com.example.skripsi.feature.auth.vm.LoginViewModelFactory
import com.example.skripsi.feature.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var vm: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val session = SessionManager(this)
        if (session.isLoggedIn) {
            goMain()
            return
        }

        vm = ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]
        vm.seed()

        binding.apply {
            btnLogin.setOnClickListener {
                val u = etUsername.text.toString().trim()
                val p = etPassword.text.toString()
                if (u.isEmpty() || p.isEmpty()){
                    Toast.makeText(this@LoginActivity, "Lengkapi username / password", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                vm.login(u, p,
                    onSuccess = { goMain() },
                    onError = { msg -> Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
