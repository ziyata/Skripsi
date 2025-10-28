package com.example.skripsi.feature.transaksi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.databinding.ItemTransaksiHeaderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TransaksiHeaderAdapter(
    private val onClick: (TransaksiHeaderEntity) -> Unit
) : RecyclerView.Adapter<TransaksiHeaderAdapter.VH>() {

    private val data = mutableListOf<TransaksiHeaderEntity>()

    fun submit(list: List<TransaksiHeaderEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemTransaksiHeaderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: TransaksiHeaderEntity) {
            val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in","ID"))
            b.tvTanggal.text = fmt.format(java.util.Date(row.tanggal))
            b.tvMetodeStatus.text = "${row.metode} • ${row.status}"
            b.tvTotal.text = "Rp %,d".format(Locale("in","ID"), row.total)
            b.root.setOnClickListener { onClick(row) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTransaksiHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}
