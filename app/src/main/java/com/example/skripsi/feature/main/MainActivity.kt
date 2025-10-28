package com.example.skripsi.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.feature.barang.ui.BarangActivity
import com.example.skripsi.feature.checkout.ui.CheckoutActivity
import com.example.skripsi.feature.prediksi.ui.PrediksiActivity
import com.example.skripsi.feature.qr.QrActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBarang.setOnClickListener {
            startActivity(Intent(this, BarangActivity::class.java))
        }
        binding.btnPrediksi.setOnClickListener {
            startActivity(Intent(this, PrediksiActivity::class.java))
        }
        binding.btnQr.setOnClickListener {
            startActivity(Intent(this, QrActivity::class.java))
        }
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}