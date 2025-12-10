package com.example.skripsi.data.repository

import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.TransaksiDetailDao
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.feature.admin.TopSellingItem

class AdminOrderRepository(
    private val transaksiDetailDao: TransaksiDetailDao,
    private val barangDao: BarangDao
) {

    suspend fun getTopSellingItems(days: Int = 7): List<TopSellingItem> {
        val startDate = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return transaksiDetailDao.getTopSellingItems(startDate)
    }

    data class SafetyStockItem(
        val barangId: Int,
        val nama: String,
        val stokSaatIni: Int,
        val avgDailyQty: Double,
        val safetyStock: Int,
        val status: String   // "AMAN" / "RAWAN"
    )

    suspend fun getSafetyStockList(
        daysHistory: Int = 30,
        bufferDays: Int = 3
    ): List<SafetyStockItem> {
        val startDate = System.currentTimeMillis() - (daysHistory * 24 * 60 * 60 * 1000L)
        val usage = transaksiDetailDao.getDailyUsage(startDate)
        val barangList: List<BarangEntity> = barangDao.getAllOnce()   // fungsi baru di BarangDao

        val barangMap = barangList.associateBy { it.id }

        return usage.mapNotNull { u ->
            val barang = barangMap[u.barangId] ?: return@mapNotNull null
            val safety = (u.avgDailyQty * bufferDays).toInt().coerceAtLeast(1)
            val status = if (barang.stok <= safety) "RAWAN" else "AMAN"

            SafetyStockItem(
                barangId = u.barangId,
                nama = u.nama,
                stokSaatIni = barang.stok,
                avgDailyQty = u.avgDailyQty,
                safetyStock = safety,
                status = status
            )
        }
    }
}
