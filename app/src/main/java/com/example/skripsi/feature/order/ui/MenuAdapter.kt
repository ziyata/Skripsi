package com.example.skripsi.feature.order.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.databinding.ItemMenuBinding

class MenuAdapter(
    private val onClick: (BarangEntity) -> Unit
) : RecyclerView.Adapter<MenuAdapter.VH>() {

    private val data = mutableListOf<BarangEntity>()

    fun submit(list: List<BarangEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemMenuBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: BarangEntity) {
            b.tvNama.text = item.nama
            b.tvHarga.text = CurrencyFormatter.rupiah(item.harga)
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
