package com.example.skripsi.ui.transaksi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
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
    private lateinit var tvInfoHeader: TextView
    private lateinit var tvTotal: TextView
    private lateinit var rvDetail: RecyclerView
    private lateinit var vm: TransaksiDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_detail)

        tvInfoHeader = findViewById(R.id.tvInfoHeader)
        tvTotal = findViewById(R.id.tvTotal)
        rvDetail = findViewById(R.id.rvDetail)

        adapter = TransaksiDetailAdapter()
        rvDetail.layoutManager = LinearLayoutManager(this)
        rvDetail.adapter = adapter

        vm = ViewModelProvider(this, TransaksiDetailViewModelFactory(this))[TransaksiDetailViewModel::class.java]

        val headerId = intent.getIntExtra(EXTRA_HEADER_ID, -1)
        val total = intent.getLongExtra(EXTRA_TOTAL, 0L)
        val metode = intent.getStringExtra(EXTRA_METODE) ?: "CASH"
        val tanggal = intent.getLongExtra(EXTRA_TANGGAL, 0L)

        val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in","ID"))
        tvInfoHeader.text = "Metode: $metode • Tanggal: ${fmt.format(java.util.Date(tanggal))}"
        tvTotal.text = "Total: Rp %,d".format(Locale("in","ID"), total)

        vm.details.observe(this) { list ->
            adapter.submit(list)
        }

        if (headerId > 0) {
            vm.load(headerId)
        } else {
            android.widget.Toast.makeText(this, "Header ID tidak valid", android.widget.Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
