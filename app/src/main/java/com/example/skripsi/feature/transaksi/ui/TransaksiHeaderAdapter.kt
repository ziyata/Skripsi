package com.example.skripsi.feature.transaksi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.databinding.ItemTransaksiHeaderBinding
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.core.util.DateUtil

class TransaksiHeaderAdapter(
    private val onClick: (TransaksiHeaderEntity) -> Unit
) : RecyclerView.Adapter<TransaksiHeaderAdapter.VH>() {

    private val data = mutableListOf<TransaksiHeaderEntity>()

    fun submit(list: List<TransaksiHeaderEntity>) { data.apply { clear(); addAll(list) }; notifyDataSetChanged() }

    inner class VH(private val b: ItemTransaksiHeaderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: TransaksiHeaderEntity) {
            b.tvTanggal.text = DateUtil.longToHuman(row.tanggal)
            b.tvMetodeStatus.text = "${row.metode} • ${row.status}"
            b.tvTotal.text = CurrencyFormatter.rupiah(row.total)
            b.root.setOnClickListener { onClick(row) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransaksiHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])
    override fun getItemCount(): Int = data.size
}
