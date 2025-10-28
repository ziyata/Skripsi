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
import com.example.skripsi.R
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModel
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModelFactory
import androidx.recyclerview.widget.RecyclerView

class TransaksiListActivity : AppCompatActivity() {
    private lateinit var vm: TransaksiListViewModel
    private lateinit var adapter: TransaksiHeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_list)

        val et = findViewById<EditText>(R.id.etSearch)
        val btnToday = findViewById<Button>(R.id.btnFilterToday)
        val rv = findViewById<RecyclerView>(R.id.rvTransaksi)

        vm = ViewModelProvider(this, TransaksiListViewModelFactory(this))[TransaksiListViewModel::class.java]
        adapter = TransaksiHeaderAdapter { h ->
            startActivity(Intent(this, TransaksiDetailActivity::class.java).apply {
                putExtra(TransaksiDetailActivity.EXTRA_HEADER_ID, h.id)
                putExtra(TransaksiDetailActivity.EXTRA_TOTAL, h.total)
                putExtra(TransaksiDetailActivity.EXTRA_METODE, h.metode)
                putExtra(TransaksiDetailActivity.EXTRA_TANGGAL, h.tanggal)
            })
        }
        rv.layoutManager = LinearLayoutManager(this); rv.adapter = adapter

        vm.filtered.observe(this) { adapter.submit(it) }

        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun afterTextChanged(s: Editable?) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { vm.setQuery(s?.toString().orEmpty()) }
        })

        var today = false
        btnToday.setOnClickListener { today = !today; vm.setTodayOnly(today); btnToday.text = if (today) "Semua" else "Hari ini" }
    }
}
