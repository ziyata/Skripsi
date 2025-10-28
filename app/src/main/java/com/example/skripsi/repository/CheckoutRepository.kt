package com.example.skripsi.repository

import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.StockMutationDao
import com.example.skripsi.data.dao.TransaksiDetailDao
import com.example.skripsi.data.dao.TransaksiHeaderDao
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.StockMutationEntity
import com.example.skripsi.data.entity.TransaksiDetailEntity
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckoutRepository(
    private val barangDao: BarangDao,
    private val headerDao: TransaksiHeaderDao,
    private val detailDao: TransaksiDetailDao,
    private val stockMutationDao: StockMutationDao
) {

    data class CartItem(
        val barang: BarangEntity,
        val qty: Int
    ) {
        val subtotal: Long get() = barang.harga * qty.toLong()
    }

    suspend fun checkout(
        items: List<CartItem>,
        metode: String
    ): Int = withContext(Dispatchers.IO) {
        if (items.isEmpty()) throw IllegalArgumentException("Cart kosong")

        val total = items.sumOf { it.subtotal }

        val headerId = headerDao.insert(
            TransaksiHeaderEntity(
                tanggal = System.currentTimeMillis(),
                total = total,
                metode = metode,
                status = "PAID",
                customerId = null
            )
        ).toInt()

        val details = items.map {
            TransaksiDetailEntity(
                transaksiId = headerId,
                barangId = it.barang.id,
                namaBarangSnapshot = it.barang.nama,
                hargaSatuan = it.barang.harga,
                qty = it.qty,
                subtotal = it.subtotal
            )
        }
        detailDao.insertAll(details)

        for (ci in items) {
            val barang = barangDao.getBarangById(ci.barang.id) ?: continue
            val sisa = barang.stok - ci.qty
            if (sisa < 0) throw IllegalStateException("Stok ${barang.nama} kurang")
            barangDao.updateBarang(barang.copy(stok = sisa))

            stockMutationDao.insert(
                StockMutationEntity(
                    barangId = barang.id,
                    tipe = "OUT",
                    qty = ci.qty,
                    waktu = System.currentTimeMillis(),
                    refType = "TRANSACTION",
                    refId = headerId
                )
            )
        }

        headerId
    }
}
