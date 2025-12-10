package com.example.skripsi.feature.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.repository.AdminOrderRepository
import com.example.skripsi.databinding.ActivityTopSellingBinding

class TopSellingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTopSellingBinding

    private val vm: TopSellingViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        val repo = AdminOrderRepository(db.transaksiDetailDao(),db.barangDao())
        TopSellingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopSellingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()

        vm.topItems.observe(this) { items ->
            (binding.rvTopSelling.adapter as? TopSellingAdapter)?.submitList(items)
        }

        vm.loadTopSelling()
    }

    private fun setupRecycler() {
        binding.rvTopSelling.apply {
            layoutManager = LinearLayoutManager(this@TopSellingActivity)
            adapter = TopSellingAdapter()
        }
    }
}
