package com.example.skripsi.feature.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.repository.AdminOrderRepository
import com.example.skripsi.databinding.ActivitySafetyStockBinding

class SafetyStockActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySafetyStockBinding

    private val vm: SafetyStockViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        val repo = AdminOrderRepository(db.transaksiDetailDao(), db.barangDao())
        SafetyStockViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafetyStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = SafetyStockAdapter()
        binding.rvSafetyStock.layoutManager = LinearLayoutManager(this)
        binding.rvSafetyStock.adapter = adapter

        vm.items.observe(this) { list ->
            adapter.submitList(list)
        }

        vm.loadSafetyStock()
    }
}
