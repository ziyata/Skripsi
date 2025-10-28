package com.example.skripsi.feature.checkout.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.skripsi.R
import com.example.skripsi.data.entity.BarangEntity
import java.util.Locale

class PilihBarangGridAdapter(
    private val context: Context,
    private var list: List<BarangEntity>
) : BaseAdapter() {

    fun submit(newList: List<BarangEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_pilih_barang, parent, false)

        val barang = list[position]
        view.findViewById<TextView>(R.id.tvNamaBarang).text = barang.nama
        view.findViewById<TextView>(R.id.tvHargaBarang).text =
            "Rp %,d".format(Locale("in","ID"), barang.harga)
        view.findViewById<TextView>(R.id.tvStokBarang).text = "Stok: ${barang.stok}"

        return view
    }
}
