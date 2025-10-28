package com.example.skripsi.repository

import androidx.lifecycle.LiveData
import com.example.skripsi.data.dao.OrderDao
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity

class OrderRepository(private val orderDao: OrderDao) {

    suspend fun createDraft(
        tableId: String?,
        customerName: String?,
        notes: String?,
        items: List<OrderDetailEntity>
    ): Int {
        val headerId = orderDao.insertHeader(
            OrderHeaderEntity(
                createdAt = System.currentTimeMillis(),
                tableId = tableId,
                customerName = customerName,
                status = "DRAFT",
                notes = notes
            )
        ).toInt()
        val detailWithId = items.map { it.copy(orderId = headerId) }
        orderDao.insertDetails(detailWithId)
        return headerId
    }

    fun listByStatus(status: String): LiveData<List<OrderHeaderEntity>> =
        orderDao.listByStatus(status)

    suspend fun getDetails(orderId: Int): List<OrderDetailEntity> =
        orderDao.getDetails(orderId)

    suspend fun updateStatus(orderId: Int, status: String) =
        orderDao.updateStatus(orderId, status)
}
