package com.example.skripsi.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.feature.auth.ui.LoginActivity
import com.example.skripsi.feature.barang.ui.BarangActivity
import com.example.skripsi.feature.checkout.ui.CheckoutActivity
import com.example.skripsi.feature.prediksi.ui.PrediksiActivity
import com.example.skripsi.feature.qr.QrActivity
import com.example.skripsi.feature.stok.ui.StockAdjustmentActivity
import com.example.skripsi.feature.transaksi.ui.TransaksiListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        // Tampilkan sesuai role
        binding.tvAdminSection.isVisible = session.isAdmin
        binding.btnBarang.isVisible = session.isAdmin
        binding.btnStockAdjustment.isVisible = session.isAdmin
        binding.btnPrediksi.isVisible = session.isAdmin
        binding.btnRiwayatTransaksi.isVisible = session.isAdmin

        binding.tvUserSection.isVisible = !session.isAdmin
        binding.btnScanQr.isVisible = !session.isAdmin
        binding.btnCheckout.isVisible = !session.isAdmin

        // Aksi tombol
        binding.btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnBarang.setOnClickListener {
            startActivity(Intent(this, BarangActivity::class.java))
        }
        binding.btnStockAdjustment.setOnClickListener {
            startActivity(Intent(this, StockAdjustmentActivity::class.java))
        }
        binding.btnPrediksi.setOnClickListener {
            startActivity(Intent(this, PrediksiActivity::class.java))
        }
        binding.btnRiwayatTransaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiListActivity::class.java))
        }
        binding.btnScanQr.setOnClickListener {
            startActivity(Intent(this, QrActivity::class.java))
        }
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}
