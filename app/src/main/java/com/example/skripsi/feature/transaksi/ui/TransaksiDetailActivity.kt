package com.example.skripsi.feature.transaksi.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.databinding.ActivityTransaksiDetailBinding
import com.example.skripsi.feature.transaksi.vm.TransaksiDetailViewModel
import com.example.skripsi.feature.transaksi.vm.TransaksiDetailViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class TransaksiDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HEADER_ID = "extra_header_id"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_METODE = "extra_metode"
        const val EXTRA_TANGGAL = "extra_tanggal"
    }

    private lateinit var adapter: TransaksiDetailAdapter
    private lateinit var vm: TransaksiDetailViewModel
    private lateinit var binding: ActivityTransaksiDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            adapter = TransaksiDetailAdapter()
            rvDetail.layoutManager = LinearLayoutManager(this@TransaksiDetailActivity)
            rvDetail.adapter = adapter

            vm = ViewModelProvider(this@TransaksiDetailActivity, TransaksiDetailViewModelFactory(this@TransaksiDetailActivity))[TransaksiDetailViewModel::class.java]

            val headerId = intent.getIntExtra(EXTRA_HEADER_ID, -1)
            val total = intent.getLongExtra(EXTRA_TOTAL, 0L)
            val metode = intent.getStringExtra(EXTRA_METODE) ?: "CASH"
            val tanggal = intent.getLongExtra(EXTRA_TANGGAL, 0L)

            val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in","ID"))
            tvInfoHeader.text = "Metode: $metode • Tanggal: ${fmt.format(java.util.Date(tanggal))}"
            tvTotal.text = "Total: Rp %,d".format(Locale("in","ID"), total)

            vm.details.observe(this@TransaksiDetailActivity) { list ->
                adapter.submit(list)
            }

            if (headerId > 0) {
                vm.load(headerId)
            } else {
                android.widget.Toast.makeText(this@TransaksiDetailActivity, "Header ID tidak valid", android.widget.Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
