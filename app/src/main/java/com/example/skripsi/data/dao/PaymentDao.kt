package com.example.skripsi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.skripsi.data.entity.PaymentEntity

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(payment: PaymentEntity): Long
}
