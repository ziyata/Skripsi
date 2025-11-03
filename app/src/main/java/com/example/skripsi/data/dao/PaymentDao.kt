package com.example.skripsi.data.dao

import androidx.room.*
import com.example.skripsi.data.entity.PaymentEntity

@Dao
interface PaymentDao {

    @Insert
    suspend fun insert(payment: PaymentEntity): Long

    @Query("UPDATE payment SET status = :status WHERE orderId = :orderId")
    suspend fun updateStatusByOrder(orderId: Int, status: String)

    @Query("SELECT * FROM payment WHERE orderId = :orderId LIMIT 1")
    suspend fun getByOrder(orderId: Int): PaymentEntity?
}
