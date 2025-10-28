package com.example.skripsi.ui.barang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.skripsi.R
import com.example.skripsi.data.entity.BarangEntity
import java.util.Locale

class BarangGridAdapter(
    private val context: Context,
    private val list: List<BarangEntity>
) : BaseAdapter() {

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_barang, parent, false)

        val barang = list[position]
        val tvNama = view.findViewById<TextView>(R.id.tvNamaBarang)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaBarang)
        val tvStok = view.findViewById<TextView>(R.id.tvStokBarang)

        tvNama.text = barang.nama
        tvHarga.text = "Rp %,d".format(Locale("in", "ID"), barang.harga)
        tvStok.text = "Stok: ${barang.stok}"

        return view
    }
}
