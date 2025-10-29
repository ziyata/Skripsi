package com.example.skripsi.feature.order.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.databinding.ItemOrderDetailBinding

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.VH>() {

    private val data = mutableListOf<OrderDetailEntity>()

    fun submit(list: List<OrderDetailEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemOrderDetailBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: OrderDetailEntity) {
            b.tvNama.text = row.namaSnapshot
            b.tvQty.text = row.qty.toString()
            b.tvHarga.text = CurrencyFormatter.rupiah(row.hargaSatuan)
            b.tvSubtotal.text = CurrencyFormatter.rupiah(row.subtotal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}
