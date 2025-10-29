package com.example.skripsi.feature.order.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.databinding.ActivityOrderBinding
import com.example.skripsi.feature.order.vm.OrderViewModel
import com.example.skripsi.feature.order.vm.OrderViewModelFactory
import com.example.skripsi.feature.qr.ui.QrActivity

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var vm: OrderViewModel
    private lateinit var adapter: OrderAdapter

    private var tableId: Int? = null

    private val qrLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == RESULT_OK && res.data != null) {
            when (res.data!!.getStringExtra("qr_type")) {
                "item" -> {
                    val id = res.data!!.getIntExtra("item_id", -1)
                    if (id > 0) vm.addItem(id, 1)
                }
                "table" -> {
                    tableId = res.data!!.getIntExtra("table_id", -1).takeIf { it > 0 }
                    vm.initDraft(tableId)
                }
                "cart" -> {
                    // decode payload jika diperlukan untuk mass add
                }
            }
        }
    }

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, OrderViewModelFactory(this))[OrderViewModel::class.java]
        adapter = OrderAdapter()

        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = adapter

        // Inisialisasi draft tanpa table jika belum ada
        vm.initDraft(tableId)

        vm.items.observe(this) { list ->
            adapter.submit(list)
            binding.tvTotal.text = "Total: ${CurrencyFormatter.rupiah(vm.total)}"
        }

        binding.btnScanTambah.setOnClickListener {
            qrLauncher.launch(Intent(this, QrActivity::class.java))
        }

        binding.btnKirim.setOnClickListener {
            // Kirim ke status UNPAID (admin akan konfirmasi)
            val id = vm.orderId
            if (id == null) {
                Toast.makeText(this, "Order belum siap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: update status UNPAID di repository + buat PaymentEntity method=TRANSFER status=PENDING
            Toast.makeText(this, "Pesanan dikirim (UNPAID). Tunjukkan ke kasir.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
