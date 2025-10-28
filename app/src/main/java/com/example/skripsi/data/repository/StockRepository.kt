package com.example.skripsi.data.repository

import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.StockMutationDao
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.StockMutationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StockRepository(
    private val barangDao: BarangDao,
    private val mutationDao: StockMutationDao
) {
    suspend fun adjustIn(barangId: Int, qty: Int, note: String?) = withContext(Dispatchers.IO) {
        val barang = barangDao.getBarangById(barangId) ?: throw IllegalArgumentException("Barang tidak ditemukan")
        val newStock = barang.stok + qty
        barangDao.updateBarang(barang.copy(stok = newStock))
        mutationDao.insert(
            StockMutationEntity(
                barangId = barangId,
                tipe = "IN",
                qty = qty,
                waktu = System.currentTimeMillis(),
                refType = "ADJUSTMENT",
                refId = null
            )
        )
    }

    suspend fun adjustCorrection(barangId: Int, qtyDiff: Int, note: String?) = withContext(Dispatchers.IO) {
        val barang = barangDao.getBarangById(barangId) ?: throw IllegalArgumentException("Barang tidak ditemukan")
        val newStock = (barang.stok + qtyDiff).coerceAtLeast(0)
        val tipe = if (qtyDiff >= 0) "IN" else "ADJUST"
        barangDao.updateBarang(barang.copy(stok = newStock))
        mutationDao.insert(
            StockMutationEntity(
                barangId = barangId,
                tipe = tipe,
                qty = kotlin.math.abs(qtyDiff),
                waktu = System.currentTimeMillis(),
                refType = "ADJUSTMENT",
                refId = null
            )
        )
    }

    suspend fun getBarangById(id: Int): BarangEntity? = withContext(Dispatchers.IO) {
        barangDao.getBarangById(id)
    }

    suspend fun listMutasi(barangId: Int) = withContext(Dispatchers.IO) {
        mutationDao.listByBarang(barangId)
    }
}
