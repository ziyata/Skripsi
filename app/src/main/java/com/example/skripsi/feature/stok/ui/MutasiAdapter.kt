package com.example.skripsi.feature.stok.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.entity.StockMutationEntity
import com.example.skripsi.databinding.ItemMutasiStokBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MutasiAdapter : RecyclerView.Adapter<MutasiAdapter.VH>() {
    private val data = mutableListOf<StockMutationEntity>()
    fun submit(list: List<StockMutationEntity>) { data.apply { clear(); addAll(list) }; notifyDataSetChanged() }

    inner class VH(private val b: ItemMutasiStokBinding) : RecyclerView.ViewHolder(b.root) {
        private val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in","ID"))
        fun bind(m: StockMutationEntity) {
            b.tvTipe.text = m.tipe
            b.tvQty.text = m.qty.toString()
            b.tvWaktu.text = fmt.format(Date(m.waktu))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMutasiStokBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])
    override fun getItemCount(): Int = data.size
}
