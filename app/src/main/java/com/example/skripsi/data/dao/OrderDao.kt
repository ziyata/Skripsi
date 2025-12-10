package com.example.skripsi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity

@Dao
interface OrderDao {

    @Insert
    suspend fun insertHeader(h: OrderHeaderEntity): Long

    @Insert
    suspend fun insertDetail(d: OrderDetailEntity): Long

    @Update
    suspend fun updateDetail(d: OrderDetailEntity)

    @Query("SELECT * FROM order_header WHERE status IN ('DRAFT','UNPAID') AND (tableId IS :tableId OR (:tableId IS NULL AND tableId IS NULL)) LIMIT 1")
    suspend fun findDraft(tableId: String?): OrderHeaderEntity?

    @Query("SELECT * FROM order_detail WHERE orderId = :orderId AND barangId = :barangId LIMIT 1")
    suspend fun findDetail(orderId: Int, barangId: Int): OrderDetailEntity?

    @Query("SELECT * FROM order_detail WHERE orderId = :orderId")
    suspend fun listDetails(orderId: Int): List<OrderDetailEntity>

    @Query("DELETE FROM order_detail WHERE orderId = :orderId")
    suspend fun deleteDetails(orderId: Int)

    @Query("DELETE FROM order_header WHERE id = :orderId")
    suspend fun deleteHeader(orderId: Int)

    @Query("UPDATE order_header SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: Int, status: String)

    @Query("UPDATE order_header SET total = :total WHERE id = :orderId")
    suspend fun updateTotal(orderId: Int, total: Long)

    @Query("SELECT * FROM order_header WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Int): OrderHeaderEntity?

    @Query("SELECT * FROM order_header WHERE status = 'UNPAID' ORDER BY createdAt DESC")
    suspend fun listUnpaidOrders(): List<OrderHeaderEntity>
}
