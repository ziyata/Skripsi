package com.example.skripsi.feature.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.repository.AdminOrderRepository
import com.example.skripsi.databinding.ItemSafetyStockBinding
import kotlin.math.round

class SafetyStockAdapter :
    RecyclerView.Adapter<SafetyStockAdapter.VH>() {

    private val data = mutableListOf<AdminOrderRepository.SafetyStockItem>()

    fun submitList(items: List<AdminOrderRepository.SafetyStockItem>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSafetyStockBinding.inflate(
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
        private val binding: ItemSafetyStockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AdminOrderRepository.SafetyStockItem) {
            binding.tvNamaBarang.text = item.nama
            binding.tvStok.text = "Stok: ${item.stokSaatIni}"
            binding.tvAvgDaily.text = "Rata-rata harian: ${round(item.avgDailyQty * 100) / 100.0}"
            binding.tvSafety.text = "Safety stock: ${item.safetyStock}"

            binding.tvStatus.text = "Status: ${item.status}"
            binding.tvStatus.setTextColor(
                if (item.status == "RAWAN") Color.RED else Color.GREEN
            )
        }
    }
}
