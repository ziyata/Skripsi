package com.example.skripsi.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import com.example.skripsi.core.util.SessionManager
import com.example.skripsi.databinding.ActivityMainBinding
import com.example.skripsi.feature.auth.LoginActivity
import com.example.skripsi.feature.barang.ui.BarangActivity
import com.example.skripsi.feature.order.ui.OrderActivity
import com.example.skripsi.feature.order.ui.AdminOrderActivity
import com.example.skripsi.feature.qr.ui.QrActivity
import com.example.skripsi.feature.transaksi.ui.TransaksiListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    private val qrLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val qrType = result.data!!.getStringExtra("qr_type")
            val qrValue = result.data!!.getStringExtra("qr_value")

            when (qrType) {
                "table" -> {
                    val intent = Intent(this, OrderActivity::class.java)
                    intent.putExtra("tableId", qrValue)
                    startActivity(intent)
                }
            }
        }
    }

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Cek jika tidak login, redirect ke Login
        if (!session.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Setup UI berdasarkan role
        setupMenuByRole()

//         Button handlers (Admin)
        binding.btnKelolaBarang.setOnClickListener {
            startActivity(Intent(this, BarangActivity::class.java))
        }

        binding.btnPenyesuaianStok.setOnClickListener {
        }

        binding.btnPrediksi.setOnClickListener {
        }

        binding.btnRiwayatTransaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiListActivity::class.java))
        }

        binding.btnPesananMasuk.setOnClickListener {
            startActivity(Intent(this, AdminOrderActivity::class.java))
        }

        // Button handlers (User)
        binding.btnScanQr.setOnClickListener {
            val intent = Intent(this, QrActivity::class.java)
            qrLauncher.launch(intent)
        }

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupMenuByRole() {
        val role = session.userRole

        when (role) {
            "ADMIN" -> {
                // Show menu admin, hide menu user
                binding.tvAdminSection.visibility = View.VISIBLE
                binding.btnKelolaBarang.visibility = View.VISIBLE
                binding.btnPenyesuaianStok.visibility = View.VISIBLE
                binding.btnPrediksi.visibility = View.VISIBLE
                binding.btnRiwayatTransaksi.visibility = View.VISIBLE
                binding.btnPesananMasuk.visibility = View.VISIBLE

                binding.tvUserSection.visibility = View.GONE
                binding.btnScanQr.visibility = View.GONE
                binding.btnCheckout.visibility = View.GONE
            }
            "USER" -> {
                // Show menu user, hide menu admin
                binding.tvUserSection.visibility = View.GONE
                binding.btnKelolaBarang.visibility = View.GONE
                binding.btnPenyesuaianStok.visibility = View.GONE
                binding.btnPrediksi.visibility = View.GONE
                binding.btnRiwayatTransaksi.visibility = View.GONE
                binding.btnPesananMasuk.visibility = View.GONE

                binding.tvUserSection.visibility = View.VISIBLE
                binding.btnScanQr.visibility = View.VISIBLE
                binding.btnCheckout.visibility = View.VISIBLE
            }
            else -> {
                // Default: hide all (safety)
                binding.tvAdminSection.visibility = View.VISIBLE
                binding.btnKelolaBarang.visibility = View.VISIBLE
                binding.btnPenyesuaianStok.visibility = View.VISIBLE
                binding.btnPrediksi.visibility = View.VISIBLE
                binding.btnRiwayatTransaksi.visibility = View.VISIBLE
                binding.btnPesananMasuk.visibility = View.VISIBLE

                binding.tvUserSection.visibility = View.VISIBLE
                binding.btnScanQr.visibility = View.VISIBLE
                binding.btnCheckout.visibility = View.VISIBLE
            }
        }
    }
}
