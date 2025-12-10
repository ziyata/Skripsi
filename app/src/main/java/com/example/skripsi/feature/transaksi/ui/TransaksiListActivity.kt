package com.example.skripsi.feature.transaksi.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsi.databinding.ActivityTransaksiListBinding
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModel
import com.example.skripsi.feature.transaksi.vm.TransaksiListViewModelFactory

class TransaksiListActivity : AppCompatActivity() {

    private lateinit var vm: TransaksiListViewModel
    private lateinit var adapter: TransaksiHeaderAdapter
    private lateinit var binding: ActivityTransaksiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        vm = ViewModelProvider(this, TransaksiListViewModelFactory(this))[TransaksiListViewModel::class.java]
        adapter = TransaksiHeaderAdapter { h ->
            startActivity(Intent(this, TransaksiDetailActivity::class.java).apply {
                putExtra(TransaksiDetailActivity.EXTRA_HEADER_ID, h.id)
                putExtra(TransaksiDetailActivity.EXTRA_TOTAL, h.total)
                putExtra(TransaksiDetailActivity.EXTRA_METODE, h.metode)
                putExtra(TransaksiDetailActivity.EXTRA_TANGGAL, h.tanggal)
            })
        }

        binding.apply {
            rvTransaksi.layoutManager = LinearLayoutManager(this@TransaksiListActivity); rvTransaksi.adapter = adapter

            vm.filtered.observe(this@TransaksiListActivity) { adapter.submit(it) }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun afterTextChanged(s: Editable?) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { vm.setQuery(s?.toString().orEmpty()) }
            })

            var today = false
            btnFilterToday.setOnClickListener { today = !today; vm.setTodayOnly(today); btnFilterToday.text = if (today) "Semua" else "Hari ini" }
        }
    }
}
