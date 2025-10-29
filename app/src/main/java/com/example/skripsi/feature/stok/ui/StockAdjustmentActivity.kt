package com.example.skripsi.feature.stok.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.R
import com.example.skripsi.databinding.ActivityStockAdjustmentBinding
import com.example.skripsi.databinding.ActivityStockHistoryBinding
import com.example.skripsi.feature.stok.vm.StockViewModel
import com.example.skripsi.feature.stok.vm.StockViewModelFactory

class StockAdjustmentActivity : AppCompatActivity() {

    private lateinit var vm: StockViewModel
    private lateinit var adapter: MutasiAdapter
    private lateinit var binding: ActivityStockAdjustmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockAdjustmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        vm = ViewModelProvider(this, StockViewModelFactory(this))[StockViewModel::class.java]

        binding.apply {
            adapter = MutasiAdapter()
            rvMutasi.layoutManager = LinearLayoutManager(this@StockAdjustmentActivity)
            rvMutasi.adapter = adapter

            fun currentBarangId(): Int = etBarangId.text.toString().toIntOrNull() ?: -1
            fun currentQty(): Int = etQty.text.toString().toIntOrNull() ?: 0
            fun currentNote(): String? = etNote.text?.toString()

            btnIn.setOnClickListener {
                val id = currentBarangId(); val qty = currentQty()
                if (id > 0 && qty > 0) vm.addIn(id, qty, currentNote())
            }
            btnAdjust.setOnClickListener {
                val id = currentBarangId(); val diff = currentQty()
                if (id > 0 && diff != 0) vm.correct(id, diff, currentNote())
            }

            etBarangId.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val id = currentBarangId()
                    if (id > 0) vm.loadMutasi(id)
                }
            }

            vm.mutasi.observe(this@StockAdjustmentActivity) { adapter.submit(it) }
        }
    }
}
