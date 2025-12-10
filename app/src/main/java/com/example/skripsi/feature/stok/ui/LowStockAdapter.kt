package com.example.skripsi.feature.stok.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.databinding.ItemAdminOrderBinding

class LowStockAdapter :
    ListAdapter<BarangEntity, LowStockAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAdminOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BarangEntity) {
//            binding.tvNamaBarang.text = item.nama
//            binding.tvHargaBarang.text =
//                "Rp %,d".format(Locale("in", "ID"), item.harga)
//            binding.tvStokBarang.text = "Stok: ${item.stok}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<BarangEntity>() {
        override fun areItemsTheSame(oldItem: BarangEntity, newItem: BarangEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BarangEntity, newItem: BarangEntity) =
            oldItem == newItem
    }
}