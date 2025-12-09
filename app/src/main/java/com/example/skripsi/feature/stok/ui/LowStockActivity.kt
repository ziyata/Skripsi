package com.example.skripsi.feature.stok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.repository.BarangRepository
import com.example.skripsi.databinding.ActivityLowStockBinding
import com.example.skripsi.feature.stok.vm.LowStockViewModel
import com.example.skripsi.feature.stok.vm.LowStockViewModelFactory

class LowStockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLowStockBinding
    private lateinit var vm: LowStockViewModel
    private lateinit var adapter: LowStockAdapter   // adapter sederhana

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLowStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val repo = BarangRepository(db.barangDao())
        vm = ViewModelProvider(this, LowStockViewModelFactory(repo))[LowStockViewModel::class.java]

        adapter = LowStockAdapter()
        binding.rvLowStock.layoutManager = LinearLayoutManager(this)
        binding.rvLowStock.adapter = adapter

        vm.load(minStok = 5)
    }
}
