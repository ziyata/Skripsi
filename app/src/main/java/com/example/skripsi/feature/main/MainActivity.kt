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

        binding.apply {
            tvAdminSection.isVisible = session.isAdmin
            btnBarang.isVisible = session.isAdmin
            btnStockAdjustment.isVisible = session.isAdmin
            btnPrediksi.isVisible = session.isAdmin
            btnRiwayatTransaksi.isVisible = session.isAdmin

            tvUserSection.isVisible = !session.isAdmin
            btnScanQr.isVisible = !session.isAdmin
            btnCheckout.isVisible = !session.isAdmin

            btnLogout.setOnClickListener {
                session.clear()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            btnBarang.setOnClickListener {
                startActivity(Intent(this@MainActivity, BarangActivity::class.java))
            }
            btnStockAdjustment.setOnClickListener {
                startActivity(Intent(this@MainActivity, StockAdjustmentActivity::class.java))
            }
            btnPrediksi.setOnClickListener {
                startActivity(Intent(this@MainActivity, PrediksiActivity::class.java))
            }
            btnRiwayatTransaksi.setOnClickListener {
                startActivity(Intent(this@MainActivity, TransaksiListActivity::class.java))
            }
            btnScanQr.setOnClickListener {
                startActivity(Intent(this@MainActivity, QrActivity::class.java))
            }
            btnCheckout.setOnClickListener {
                startActivity(Intent(this@MainActivity, CheckoutActivity::class.java))
            }
        }
    }
}
