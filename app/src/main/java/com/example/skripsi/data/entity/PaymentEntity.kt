package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment",
    indices = [Index("orderId"), Index("transaksiId")]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val orderId: Int,
    val transaksiId: Int?,

    val method: String,

    val amount: Long,
    val changeAmount: Long,
    val refNumber: String?,
    val status: String,
    val paidAt: Long
)
