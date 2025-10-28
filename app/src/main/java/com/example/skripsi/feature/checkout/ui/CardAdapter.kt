package com.example.skripsi.feature.checkout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.databinding.ItemCardBinding
import java.util.Locale

class CartAdapter(
    private val onIncrease: (barangId: Int) -> Unit,
    private val onDecrease: (barangId: Int) -> Unit,
    private val onRemove: (barangId: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    data class Row(
        val barangId: Int,
        val nama: String,
        val harga: Long,
        val qty: Int,
        val subtotal: Long
    )

    private val data = mutableListOf<Row>()

    fun submit(list: List<Row>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: Row) {
            b.tvNama.text = row.nama
            b.tvHarga.text = "Rp %,d".format(Locale("in","ID"), row.harga)
            b.tvQty.text = row.qty.toString()
            b.tvSubtotal.text = "Rp %,d".format(Locale("in","ID"), row.subtotal)

            b.btnPlus.setOnClickListener { onIncrease(row.barangId) }
            b.btnMinus.setOnClickListener { onDecrease(row.barangId) }
            b.btnHapus.setOnClickListener { onRemove(row.barangId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}
