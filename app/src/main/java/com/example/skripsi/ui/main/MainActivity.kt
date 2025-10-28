package com.example.skripsi.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skripsi.R
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.ui.barang.BarangActivity
import com.example.skripsi.ui.checkout.CheckoutActivity
import com.example.skripsi.ui.pembelian.PembelianActivity
import com.example.skripsi.ui.prediksi.PrediksiActivity
import com.example.skripsi.ui.qr.QrActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBarang.setOnClickListener {
            startActivity(Intent(this, BarangActivity::class.java))
        }
        binding.btnPembelian.setOnClickListener {
            startActivity(Intent(this, PembelianActivity::class.java))
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