package com.example.skripsi.ui.transaksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.data.entity.TransaksiDetailEntity
import com.example.skripsi.databinding.ItemTransaksiDetailBinding
import java.util.Locale

class TransaksiDetailAdapter : RecyclerView.Adapter<TransaksiDetailAdapter.VH>() {

    private val data = mutableListOf<TransaksiDetailEntity>()

    fun submit(list: List<TransaksiDetailEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(private val b: ItemTransaksiDetailBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: TransaksiDetailEntity) {
            b.tvNama.text = row.namaBarangSnapshot
            b.tvQty.text = row.qty.toString()
            b.tvHarga.text = "Rp %,d".format(Locale("in","ID"), row.hargaSatuan)
            b.tvSubtotal.text = "Rp %,d".format(Locale("in","ID"), row.subtotal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTransaksiDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}
