package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment",
    indices = [Index("orderId"), Index("transaksiId")]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int? = null,
    val transaksiId: Int? = null,
    val method: String,
    val amount: Long,
    val changeAmount: Long = 0L,
    val refNumber: String? = null,
    val status: String,
    val paidAt: Long
)
