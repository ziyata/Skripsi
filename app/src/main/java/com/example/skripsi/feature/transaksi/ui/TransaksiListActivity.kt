package com.example.skripsi.feature.transaksi.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModel
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModelFactory

class TransaksiListActivity : AppCompatActivity() {

    private lateinit var vm: TransaksiListViewModel
    private lateinit var adapter: TransaksiHeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_list)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnToday = findViewById<Button>(R.id.btnFilterToday)
        val rv = findViewById<RecyclerView>(R.id.rvTransaksi)

        vm = ViewModelProvider(this, TransaksiListViewModelFactory(this))[TransaksiListViewModel::class.java]

        adapter = TransaksiHeaderAdapter { header ->
            val intent = Intent(this, TransaksiDetailActivity::class.java).apply {
                putExtra(TransaksiDetailActivity.EXTRA_HEADER_ID, header.id)
                putExtra(TransaksiDetailActivity.EXTRA_TOTAL, header.total)
                putExtra(TransaksiDetailActivity.EXTRA_METODE, header.metode)
                putExtra(TransaksiDetailActivity.EXTRA_TANGGAL, header.tanggal)
            }
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        vm.filtered.observe(this) { list ->
            adapter.submit(list)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun afterTextChanged(s: Editable?) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setQuery(s?.toString().orEmpty())
            }
        })

        var todayOnly = false
        btnToday.setOnClickListener {
            todayOnly = !todayOnly
            vm.setTodayOnly(todayOnly)
            btnToday.text = if (todayOnly) "Semua" else "Hari ini"
        }
    }
}
