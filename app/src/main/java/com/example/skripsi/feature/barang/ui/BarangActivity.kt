package com.example.skripsi.feature.barang.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.databinding.ActivityBarangBinding
import com.example.skripsi.data.repository.BarangRepository
import com.example.skripsi.feature.barang.vm.BarangViewModel
import com.example.skripsi.feature.barang.vm.BarangViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class BarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarangBinding
    private lateinit var viewModel: BarangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val repository = BarangRepository(db.barangDao())

        setupHargaFormatter()

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
            val hargaText = binding.etHarga.text.toString()
            val stokText = binding.etStok.text.toString()

            // buang semua karakter non-digit (titik, koma, spasi, dll)
            val hargaDigits = hargaText.replace("\\D".toRegex(), "")
            val harga = hargaDigits.toLongOrNull()
            val stok = stokText.toIntOrNull()

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

    private fun setupHargaFormatter() {
        val localeID = Locale("id", "ID")
        val formatter = NumberFormat.getNumberInstance(localeID)

        val watcher = object : TextWatcher {
            private var selfChange = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (selfChange) return
                val raw = s?.toString().orEmpty()
                val digits = raw.replace("\\D".toRegex(), "")
                if (digits.isEmpty()) {
                    selfChange = true
                    val et = binding.etHarga
                    et.text?.clear()
                    selfChange = false
                    return
                }

                val value = digits.toLongOrNull() ?: return
                val formatted = formatter.format(value)

                selfChange = true
                val et = binding.etHarga
                et.text?.replace(0, et.text?.length ?: 0, formatted)

                if (formatted.isNotEmpty()) {
                    et.setSelection(et.text?.length ?: 0)

                }
                selfChange = false
            }

        }

        binding.etHarga.addTextChangedListener(watcher)
    }

}
