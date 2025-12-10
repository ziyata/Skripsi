package com.example.skripsi.feature.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import com.example.skripsi.core.util.QrPng
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.feature.admin.SafetyStockActivity
import com.example.skripsi.feature.admin.TopSellingActivity
import com.example.skripsi.feature.auth.LoginActivity
import com.example.skripsi.feature.barang.ui.BarangActivity
import com.example.skripsi.feature.order.ui.AdminOrderActivity
import com.example.skripsi.feature.order.ui.OrderActivity
import com.example.skripsi.feature.qr.QrActivity
import com.example.skripsi.feature.stok.ui.LowStockActivity
import com.example.skripsi.feature.transaksi.ui.TransaksiListActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    private val qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val qrType = result.data!!.getStringExtra("qr_type")
                val qrValue = result.data!!.getStringExtra("qr_value")

                Log.d("QR", "type=$qrType value=$qrValue")

                when (qrType) {
                    "table" -> {
                        startActivity(
                            Intent(this, OrderActivity::class.java).apply {
                                putExtra("tableId", qrValue)
                            }
                        )
                    }
                    "item" -> {
                        val itemId = qrValue?.toIntOrNull() ?: -1
                        if (itemId > 0) {
                            startActivity(
                                Intent(this, OrderActivity::class.java).apply {
                                    putExtra("addItemId", itemId)
                                }
                            )
                        }
                    }
                    "cart" -> {
                        startActivity(
                            Intent(this, OrderActivity::class.java).apply {
                                putExtra("cartPayload", qrValue)
                            }
                        )
                    }
                }
            } else {
                Log.d("QR", "No result or data null")
            }
        }

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        if (!session.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupMenuByRole()

        // Admin
        binding.btnKelolaBarang.setOnClickListener {
            startActivity(Intent(this, BarangActivity::class.java))
        }
        binding.btnRiwayatTransaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiListActivity::class.java))
        }
        binding.btnPesananMasuk.setOnClickListener {
            startActivity(Intent(this, AdminOrderActivity::class.java))
        }
        binding.btnSafetyStock.setOnClickListener {
            startActivity(Intent(this, SafetyStockActivity::class.java))
        }

        // User
        binding.btnScanQr.setOnClickListener {
            qrLauncher.launch(Intent(this, QrActivity::class.java))
        }
//        binding.btnCheckout.setOnClickListener {
//            startActivity(Intent(this, OrderActivity::class.java))
//        }

        binding.btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnPenyesuaianStok.setOnClickListener {
            startActivity(Intent(this, LowStockActivity::class.java))
        }
    }

    private fun setupMenuByRole() {
        val role = session.userRole
        if (role.equals("ADMIN", ignoreCase = true)) {
            // Tampilkan menu admin
            binding.tvAdminSection.visibility = View.VISIBLE
            binding.btnKelolaBarang.visibility = View.VISIBLE
            binding.btnPenyesuaianStok.visibility = View.VISIBLE
            binding.btnPrediksi.visibility = View.VISIBLE
            binding.btnRiwayatTransaksi.visibility = View.VISIBLE
            binding.btnPesananMasuk.visibility = View.VISIBLE
            binding.btnGenerateQr.visibility = View.VISIBLE


            binding.btnPrediksi.setOnClickListener {
                startActivity(Intent(this, TopSellingActivity::class.java))
            }

            // Generate QR otomatis untuk semua barang
            binding.btnGenerateQr.setOnClickListener {
                Toast.makeText(this, "Generate QR Meja...", Toast.LENGTH_SHORT).show()

                val payloads = listOf(
                    "table:1" to "qr_table_1",
                    "table:2" to "qr_table_2",
                    "table:3" to "qr_table_3",
                    "table:4" to "qr_table_4",
                    "table:5" to "qr_table_5",
                    "table:6" to "qr_table_6"
                )

                try {
                    payloads.forEach { (text, name) ->
                        QrPng.save(this, text, name)
                    }
                    val dir = File(getExternalFilesDir(null), "qr")
                    val list = dir.listFiles()?.joinToString("\n") { it.name } ?: "-"
                    Toast.makeText(
                        this,
                        "QR meja tersimpan di:\n${dir.absolutePath}\n$list",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("QR", "Saved tables in: ${dir.absolutePath}\n$list")
                } catch (e: Throwable) {
                    Toast.makeText(this, "Gagal generate: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("QR", "generate meja fail", e)
                }
            }


            // Sembunyikan menu user
            binding.tvUserSection.visibility = View.GONE
            binding.btnScanQr.visibility = View.GONE
//            binding.btnCheckout.visibility = View.GONE
        } else {
            // Default user
            binding.tvAdminSection.visibility = View.GONE
            binding.btnKelolaBarang.visibility = View.GONE
            binding.btnPenyesuaianStok.visibility = View.GONE
            binding.btnPrediksi.visibility = View.GONE
            binding.btnRiwayatTransaksi.visibility = View.GONE
            binding.btnPesananMasuk.visibility = View.GONE
            binding.btnGenerateQr.visibility = View.GONE

            binding.tvUserSection.visibility = View.VISIBLE
            binding.btnScanQr.visibility = View.VISIBLE
//            binding.btnCheckout.visibility = View.VISIBLEF
        }
    }
}
