package com.example.skripsi.feature.order.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.core.util.CurrencyFormatter
import com.example.skripsi.databinding.ItemAdminOrderBinding

class AdminOrderAdapter(
    private val onConfirm: (AdminOrderItem) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.VH>() {

    private val data = mutableListOf<AdminOrderItem>()

    fun submit(list: List<AdminOrderItem>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemAdminOrderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(order: AdminOrderItem) {
            b.tvOrderId.text = "Order #${order.orderId}"
            b.tvTableId.text = if (order.tableId != null) "Meja: ${order.tableId}" else "Takeaway"
            b.tvTotal.text = "Total: ${CurrencyFormatter.rupiah(order.total)}"
            b.tvMethod.text = "Metode: ${order.paymentMethod}"      // pastikan ada TextView ini di layout
            b.tvStatus.text = "Status: ${order.paymentStatus}"      // dan ini juga
            b.btnKonfirmasi.setOnClickListener { onConfirm(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size
}
