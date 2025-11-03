package com.example.skripsi.feature.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.view.isVisible
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.feature.auth.LoginActivity
import com.example.skripsi.feature.barang.ui.BarangActivity
import com.example.skripsi.feature.checkout.ui.CheckoutActivity
import com.example.skripsi.feature.order.ui.AdminOrderActivity
import com.example.skripsi.feature.prediksi.ui.PrediksiActivity
import com.example.skripsi.feature.qr.ui.QrActivity
import com.example.skripsi.feature.stok.ui.StockAdjustmentActivity
import com.example.skripsi.feature.transaksi.ui.TransaksiListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == RESULT_OK && res.data != null) {
                when (res.data!!.getStringExtra("qr_type")) {
                    "item" -> {
                        val id = res.data!!.getIntExtra("item_id", -1)
                        Toast.makeText(this, "QR item id=$id", Toast.LENGTH_SHORT).show()
                    }

                    "table" -> {
                        val tableId = res.data!!.getIntExtra("table_id", -1)
                        Toast.makeText(this, "QR table id=$tableId", Toast.LENGTH_SHORT).show()
                    }

                    "cart" -> {
                        val payload = res.data!!.getStringExtra("cart_payload").orEmpty()
                        Toast.makeText(this, "QR cart len=${payload.length}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    @OptIn(ExperimentalGetImage::class)
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
                startActivity(
                    Intent(
                        this@MainActivity,
                        BarangActivity::class.java
                    )
                )
            }
            btnStockAdjustment.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        StockAdjustmentActivity::class.java
                    )
                )
            }
            btnPrediksi.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        PrediksiActivity::class.java
                    )
                )
            }
            btnRiwayatTransaksi.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        TransaksiListActivity::class.java
                    )
                )
            }
            btnCheckout.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        CheckoutActivity::class.java
                    )
                )
            }
            btnScanQr.setOnClickListener {
                qrLauncher.launch(
                    Intent(
                        this@MainActivity,
                        QrActivity::class.java
                    )
                )
            }
            binding.btnPesananMasuk.setOnClickListener {
                startActivity(Intent(this@MainActivity, AdminOrderActivity::class.java))
            }

        }
    }
}
