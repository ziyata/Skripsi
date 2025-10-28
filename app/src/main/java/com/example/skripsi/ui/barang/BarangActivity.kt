package com.example.skripsi.ui.barang

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.data.AppDatabase
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.databinding.ActivityBarangBinding
import com.example.skripsi.repository.BarangRepository

class BarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarangBinding
    private lateinit var viewModel: BarangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val repository = BarangRepository(db.barangDao())

        val factory = BarangViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BarangViewModel::class.java]

        val adapter = BarangAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allBarang.observe(this) { list ->
            adapter.submitList(list)
        }

        binding.btnTambah.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val harga = binding.etHarga.text.toString().toLongOrNull()
            val stok = binding.etStok.text.toString().toIntOrNull()

            if (nama.isEmpty() || harga == null || harga <= 0L || stok == null || stok < 0) {
                Toast.makeText(this, "Input tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.insert(BarangEntity(nama = nama, harga = harga, stok = stok))
            binding.etNama.text?.clear()
            binding.etHarga.text?.clear()
            binding.etStok.text?.clear()
        }
    }
}
