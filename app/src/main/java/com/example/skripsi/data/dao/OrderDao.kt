package com.example.skripsi.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHeader(header: OrderHeaderEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDetails(details: List<OrderDetailEntity>)

    @Query("SELECT * FROM order_header WHERE status = :status ORDER BY createdAt DESC")
    fun listByStatus(status: String): LiveData<List<OrderHeaderEntity>>

    @Query("SELECT * FROM order_detail WHERE orderId = :orderId")
    suspend fun getDetails(orderId: Int): List<OrderDetailEntity>

    @Query("UPDATE order_header SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: Int, status: String)
}
