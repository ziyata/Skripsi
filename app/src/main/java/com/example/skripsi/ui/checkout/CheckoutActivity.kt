package com.example.skripsi.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.ui.pilihBarang.PilihBarangActivity
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var viewModel: CheckoutViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var rvCart: RecyclerView
    private lateinit var btnTambah: Button
    private lateinit var btnBayar: Button

    private val pilihBarangLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data!!
            val id = data.getIntExtra(PilihBarangActivity.EXTRA_BARANG_ID, -1)
            val nama = data.getStringExtra(PilihBarangActivity.EXTRA_BARANG_NAMA)
            val harga = data.getLongExtra(PilihBarangActivity.EXTRA_BARANG_HARGA, 0L)
            val qty = data.getIntExtra(PilihBarangActivity.EXTRA_QTY, 0)

            if (id > 0 && !nama.isNullOrBlank() && harga > 0L && qty > 0) {
                val barang = BarangEntity(id = id, nama = nama, harga = harga, stok = Int.MAX_VALUE)
                viewModel.addToCart(barang, qty)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tvTotal = findViewById(R.id.tvTotal)
        rvCart = findViewById(R.id.rvCart)
        btnTambah = findViewById(R.id.btnTambahDariBarang)
        btnBayar = findViewById(R.id.btnBayar)

        viewModel = ViewModelProvider(this, CheckoutViewModelFactory(this))[CheckoutViewModel::class.java]

        cartAdapter = CartAdapter(
            onIncrease = { id -> viewModel.increase(id) },
            onDecrease = { id -> viewModel.decrease(id) },
            onRemove = { id -> viewModel.removeFromCart(id) }
        )
        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = cartAdapter

        viewModel.cart.observe(this) { list ->
            val rows = list.map {
                CartAdapter.Row(
                    barangId = it.barang.id,
                    nama = it.barang.nama,
                    harga = it.barang.harga,
                    qty = it.qty,
                    subtotal = it.subtotal
                )
            }
            cartAdapter.submit(rows)
            tvTotal.text = "Rp %,d".format(Locale("in","ID"), viewModel.total)
        }

        btnTambah.setOnClickListener {
            val intent = Intent(this, PilihBarangActivity::class.java)
            pilihBarangLauncher.launch(intent)
        }

        btnBayar.setOnClickListener { showCashPaymentDialog() }
    }

    private fun showCashPaymentDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_cash_payment, null)
        val etBayar = view.findViewById<EditText>(R.id.etBayar)
        val tvKembalian = view.findViewById<TextView>(R.id.tvKembalian)
        val totalNow = viewModel.total
        tvKembalian.text = "Kembalian: Rp 0"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Pembayaran Tunai")
            .setView(view)
            .setPositiveButton("Bayar") { _, _ ->
                val bayar = etBayar.text.toString().toLongOrNull() ?: 0L
                if (bayar < totalNow) {
                    android.widget.Toast.makeText(this, "Uang kurang", android.widget.Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                viewModel.payCash(
                    bayar = bayar,
                    onSuccess = { headerId, change ->
                        android.widget.Toast.makeText(
                            this,
                            "Lunas. ID: $headerId, Kembalian: Rp %,d".format(Locale("in","ID"), change),
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                        // Buka detail transaksi
                        val intent = Intent(this, com.example.skripsi.ui.transaksi.TransaksiDetailActivity::class.java).apply {
                            putExtra(com.example.skripsi.ui.transaksi.TransaksiDetailActivity.EXTRA_HEADER_ID, headerId)
                            putExtra(com.example.skripsi.ui.transaksi.TransaksiDetailActivity.EXTRA_TOTAL, totalNow)
                            putExtra(com.example.skripsi.ui.transaksi.TransaksiDetailActivity.EXTRA_METODE, "CASH")
                            putExtra(com.example.skripsi.ui.transaksi.TransaksiDetailActivity.EXTRA_TANGGAL, System.currentTimeMillis())
                        }
                        startActivity(intent)
                    },
                    onError = { e ->
                        android.widget.Toast.makeText(this, "Gagal: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Batal", null)
            .create()

        etBayar.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun afterTextChanged(s: android.text.Editable?) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val bayar = s?.toString()?.toLongOrNull() ?: 0L
                val change = (bayar - totalNow).coerceAtLeast(0L)
                tvKembalian.text = "Kembalian: Rp %,d".format(Locale("in","ID"), change)
            }
        })

        dialog.show()
    }
}
