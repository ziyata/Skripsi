package com.example.skripsi.feature.order.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.databinding.ActivityOrderBinding
import com.example.skripsi.feature.order.vm.OrderViewModel
import com.example.skripsi.feature.order.vm.OrderViewModelFactory
import com.example.skripsi.feature.qr.QrActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var vm: OrderViewModel
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var menuAdapter: MenuAdapter

    private var tableId: String? = null

    // QR dari dalam OrderActivity, kalau mau scan meja lagi (opsional)
    private val qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == RESULT_OK && res.data != null) {
                val qrType = res.data!!.getStringExtra("qr_type")
                val qrValue = res.data!!.getStringExtra("qr_value")
                when (qrType) {
                    "table" -> {
                        tableId = qrValue
                        vm.initDraft(tableId)
                    }
                    // item/cart bisa diabaikan kalau sudah tidak dipakai
                }
            }
        }

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, OrderViewModelFactory(this))[OrderViewModel::class.java]

        // ====== SETUP LIST PESANAN (ORDER DETAILS) ======
        orderAdapter = OrderAdapter()
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = orderAdapter

        // ====== SETUP LIST MENU ======
        menuAdapter = MenuAdapter { barang ->
            // ketika user tap menu -> tambah 1 item ke pesanan
            vm.addItem(barang.id, 1)
        }
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = menuAdapter

        // Ambil extras dari MainActivity (scan dari menu utama)
        tableId = intent.getStringExtra("tableId")
        val addItemId = intent.getIntExtra("addItemId", -1)
        val cartPayload = intent.getStringExtra("cartPayload")

        Log.d("Order", "onCreate tableId=$tableId addItemId=$addItemId cart=${cartPayload?.take(30)}")

        // Init draft untuk meja ini
        vm.initDraft(tableId)

        // Kalau kalau masih ada alur lama (scan item/cart dari MainActivity)
        if (addItemId > 0) {
            vm.addItem(addItemId, 1)
        }
        if (!cartPayload.isNullOrBlank()) {
            try {
                val json = String(
                    android.util.Base64.decode(
                        cartPayload,
                        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP
                    )
                )
                val obj = JSONObject(json)
                val items = obj.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val it = items.getJSONObject(i)
                    val id = it.getInt("id")
                    val qty = it.getInt("qty")
                    vm.addItem(id, qty)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "QR cart tidak valid: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe list pesanan
        vm.items.observe(this) { list ->
            orderAdapter.submit(list)
            binding.tvTotal.text = "Total: ${CurrencyFormatter.rupiah(vm.total)}"
        }

        // Muat semua menu dari database
        loadMenuFromDb()

        // Tombol Scan (kalau mau gunakan untuk scan meja lagi)
        binding.btnScanTambah.setOnClickListener {
            qrLauncher.launch(Intent(this, QrActivity::class.java))
        }

        // Kirim jadi UNPAID
        binding.btnKirim.setOnClickListener {
            val id = vm.orderId
            if (id == null) {
                Toast.makeText(this, "Order belum siap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // baca pilihan metode bayar
            val method = when {
                binding.rbCash.isChecked -> "CASH"
                binding.rbEwallet.isChecked -> "EWALLET"
                else -> {
                    Toast.makeText(this, "Pilih metode bayar dulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            vm.markUnpaid(
                method = method,
                onDone = {
                    Toast.makeText(this, "Pesanan dikirim ($method). Tunjukkan ke kasir.", Toast.LENGTH_LONG).show()
                    finish()
                },
                onError = { e ->
                    Toast.makeText(this, "Gagal kirim: ${e.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun loadMenuFromDb() {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(this@OrderActivity)
                val list = withContext(Dispatchers.IO) {
                    db.barangDao().getAllBarangOnce()
                }
                menuAdapter.submit(list)
            } catch (e: Exception) {
                Toast.makeText(this@OrderActivity, "Gagal load menu: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
