package com.example.skripsi.ui.pembelian

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.R
import com.example.skripsi.data.AppDatabase
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.TransaksiEntity
import com.example.skripsi.repository.BarangRepository
import com.example.skripsi.repository.TransaksiRepository
import com.example.skripsi.ui.barang.BarangGridAdapter

class PembelianActivity : AppCompatActivity() {

    private lateinit var viewModel: PembelianViewModel
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembelian)

        gridView = findViewById(R.id.gridBarang)

        val db = AppDatabase.getDatabase(this)
        val barangRepo = BarangRepository(db.barangDao())
        val transaksiRepo = TransaksiRepository(db.transaksiDao())
        val factory = PembelianViewModelFactory(barangRepo, transaksiRepo)

        viewModel = ViewModelProvider(this, factory)[PembelianViewModel::class.java]

        viewModel.allBarang.observe(this) { list ->
            gridView.adapter = BarangGridAdapter(this, list)
            gridView.setOnItemClickListener { _, _, position, _ ->
                val barang = list[position]
                showDialogPembelian(barang)
            }
        }
    }

    private fun showDialogPembelian(barang: BarangEntity) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Jumlah beli"
        }

        AlertDialog.Builder(this)
            .setTitle("Beli ${barang.nama}")
            .setView(input)
            .setPositiveButton("Simpan") { _, _ ->
                val jumlah = input.text.toString().toIntOrNull() ?: 0
                if (jumlah <= 0) {
                    Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (jumlah > barang.stok) {
                    Toast.makeText(this, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val total: Long = barang.harga * jumlah.toLong()

                val transaksi = TransaksiEntity(
                    barangId = barang.id,
                    namaBarang = barang.nama,
                    jumlah = jumlah,
                    totalHarga = total,
                    tanggal = System.currentTimeMillis()
                )
                viewModel.insertTransaksi(transaksi)
                Toast.makeText(this, "Pembelian disimpan!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
