package com.example.skripsi.data.repository

import com.example.skripsi.data.dao.TransaksiDetailDao
import com.example.skripsi.feature.admin.TopSellingItem

class AdminOrderRepository(
    private val transaksiDetailDao: TransaksiDetailDao
) {
    suspend fun getTopSellingItems(days: Int = 7): List<TopSellingItem> {
        val startDate = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return transaksiDetailDao.getTopSellingItems(startDate)
    }
}


