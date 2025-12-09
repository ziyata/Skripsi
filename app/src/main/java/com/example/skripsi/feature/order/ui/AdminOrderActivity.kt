package com.example.skripsi.feature.order.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.databinding.ActivityAdminOrderBinding
import com.example.skripsi.feature.order.vm.AdminOrderViewModel
import com.example.skripsi.feature.order.vm.AdminOrderViewModelFactory

class AdminOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrderBinding
    private lateinit var vm: AdminOrderViewModel
    private lateinit var adapter: AdminOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, AdminOrderViewModelFactory(this))[AdminOrderViewModel::class.java]

        adapter = AdminOrderAdapter { item ->
            vm.confirm(
                item.orderId,
                onSuccess = {
                    Toast.makeText(this, "Order #${item.orderId} berhasil dikonfirmasi", Toast.LENGTH_SHORT).show()
                    vm.loadUnpaid()
                },
                onError = { e ->
                    Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter

        vm.orders.observe(this) { list ->
            adapter.submit(list)
        }

        vm.loadUnpaid()
    }
}

