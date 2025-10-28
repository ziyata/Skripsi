package com.example.skripsi.ui.pilihBarang

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.R
import com.example.skripsi.data.AppDatabase
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.repository.BarangRepository

class PilihBarangActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BARANG_ID = "extra_barang_id"
        const val EXTRA_BARANG_NAMA = "extra_barang_nama"
        const val EXTRA_BARANG_HARGA = "extra_barang_harga"
        const val EXTRA_QTY = "extra_qty"
    }

    private lateinit var grid: GridView
    private lateinit var etSearch: EditText
    private lateinit var adapter: PilihBarangGridAdapter
    private lateinit var vm: PilihBarangViewModel
    private var allData: List<BarangEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_barang)

        grid = findViewById(R.id.gridBarang)
        etSearch = findViewById(R.id.etSearch)

        val db = AppDatabase.getDatabase(this)
        val repo = BarangRepository(db.barangDao())
        val factory = PilihBarangViewModelFactory(repo)
        vm = ViewModelProvider(this, factory)[PilihBarangViewModel::class.java]

        adapter = PilihBarangGridAdapter(this, emptyList())
        grid.adapter = adapter

        vm.allBarang.observe(this) { list ->
            allData = list
            adapter.submit(list)
        }

        grid.setOnItemClickListener { _, _, position, _ ->
            val barang = adapter.getItem(position) as BarangEntity
            showQtyDialog(barang)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val q = s?.toString()?.trim()?.lowercase().orEmpty()
                val filtered = if (q.isEmpty()) allData
                else allData.filter { it.nama.lowercase().contains(q) }
                adapter.submit(filtered)
            }
        })
    }

    private fun showQtyDialog(barang: BarangEntity) {
        val input = EditText(this).apply {
            hint = "Jumlah"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        AlertDialog.Builder(this)
            .setTitle("Tambah ${barang.nama}")
            .setView(input)
            .setPositiveButton("Tambahkan") { _, _ ->
                val qty = input.text.toString().toIntOrNull() ?: 0
                if (qty <= 0) {
                    Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (qty > barang.stok) {
                    Toast.makeText(this, "Stok tidak cukup", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val data = Intent().apply {
                    putExtra(EXTRA_BARANG_ID, barang.id)
                    putExtra(EXTRA_BARANG_NAMA, barang.nama)
                    putExtra(EXTRA_BARANG_HARGA, barang.harga)
                    putExtra(EXTRA_QTY, qty)
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
