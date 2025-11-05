package com.example.skripsi.feature.order.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.data.entity.OrderHeaderEntity
import com.example.skripsi.databinding.ItemAdminOrderBinding

class AdminOrderAdapter(
    private val onConfirm: (OrderHeaderEntity) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.VH>() {

    private val data = mutableListOf<OrderHeaderEntity>()

    fun submit(list: List<OrderHeaderEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemAdminOrderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(order: OrderHeaderEntity) {
            b.tvOrderId.text = "Order #${order.id}"
            b.tvTableId.text = if (order.tableId != null) "Meja: ${order.tableId}" else "Takeaway"
            b.tvTotal.text = "Total: ${CurrencyFormatter.rupiah(order.total)}"
            b.btnKonfirmasi.setOnClickListener { onConfirm(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size
}
