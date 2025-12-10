package com.example.skripsi.feature.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.databinding.ItemTopSellingBinding

class TopSellingAdapter :
    RecyclerView.Adapter<TopSellingAdapter.VH>() {

    private val data = mutableListOf<TopSellingItem>()

    fun submitList(items: List<TopSellingItem>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTopSellingBinding.inflate(
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

    class VH(
        private val binding: ItemTopSellingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopSellingItem) {
            binding.tvNama.text = item.nama
            binding.tvQty.text = "Terjual: ${item.totalQty}"
            binding.tvRevenue.text =
                "Pendapatan: ${CurrencyFormatter.rupiah(item.totalRevenue)}"
        }
    }
}
