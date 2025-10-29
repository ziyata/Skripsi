package com.example.skripsi.data.repository

import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.OrderDao
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(
    private val orderDao: OrderDao,
    private val barangDao: BarangDao
) {

    suspend fun getOrCreateDraft(tableId: Int?): Int = withContext(Dispatchers.IO) {
        val existing = orderDao.findDraft(tableId)
        if (existing != null) existing.id else {
            orderDao.insertHeader(
                OrderHeaderEntity(
                    id = 0,
                    status = "DRAFT",
                    tableId = tableId.toString(),
                    createdAt = System.currentTimeMillis()
                )
            ).toInt()
        }
    }

    suspend fun addItem(orderId: Int, barangId: Int, qty: Int) = withContext(Dispatchers.IO) {
        val barang = barangDao.getBarangById(barangId)
            ?: throw IllegalArgumentException("Barang tidak ditemukan")
        val detail = orderDao.findDetail(orderId, barangId)
        if (detail == null) {
            orderDao.insertDetail(
                OrderDetailEntity(
                    id = 0,
                    orderId = orderId,
                    barangId = barangId,
                    namaSnapshot = barang.nama,
                    hargaSatuan = barang.harga,
                    qty = qty,
                    subtotal = barang.harga * qty.toLong()
                )
            )
        } else {
            val newQty = detail.qty + qty
            orderDao.updateDetail(
                detail.copy(
                    qty = newQty,
                    subtotal = detail.hargaSatuan * newQty.toLong()
                )
            )
        }
    }

    suspend fun listDetails(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.listDetails(orderId)
    }

    suspend fun markUnpaid(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateStatus(orderId, "UNPAID")
    }

    suspend fun clear(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.deleteDetails(orderId)
        orderDao.deleteHeader(orderId)
    }
}
