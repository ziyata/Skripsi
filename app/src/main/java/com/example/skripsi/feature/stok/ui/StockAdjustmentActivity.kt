package com.example.skripsi.feature.stok.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.R
import com.example.skripsi.feature.stok.vm.StockViewModel
import com.example.skripsi.feature.stok.vm.StockViewModelFactory

class StockAdjustmentActivity : AppCompatActivity() {

    private lateinit var vm: StockViewModel
    private lateinit var adapter: MutasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_adjustment)

        vm = ViewModelProvider(this, StockViewModelFactory(this))[StockViewModel::class.java]

        val etBarangId = findViewById<EditText>(R.id.etBarangId)
        val etQty = findViewById<EditText>(R.id.etQty)
        val etNote = findViewById<EditText>(R.id.etNote)
        val btnIn = findViewById<Button>(R.id.btnIn)
        val btnAdjust = findViewById<Button>(R.id.btnAdjust)

        val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvMutasi)
        adapter = MutasiAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        fun currentBarangId(): Int = etBarangId.text.toString().toIntOrNull() ?: -1
        fun currentQty(): Int = etQty.text.toString().toIntOrNull() ?: 0
        fun currentNote(): String? = etNote.text?.toString()

        btnIn.setOnClickListener {
            val id = currentBarangId(); val qty = currentQty()
            if (id > 0 && qty > 0) vm.addIn(id, qty, currentNote())
        }
        btnAdjust.setOnClickListener {
            val id = currentBarangId(); val diff = currentQty() // bisa negatif untuk pengurangan
            if (id > 0 && diff != 0) vm.correct(id, diff, currentNote())
        }

        // Auto muat riwayat saat id valid
        etBarangId.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val id = currentBarangId()
                if (id > 0) vm.loadMutasi(id)
            }
        }

        vm.mutasi.observe(this) { adapter.submit(it) }
    }
}
