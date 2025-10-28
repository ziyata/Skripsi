package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_detail",
    indices = [Index("orderId")]
)
data class OrderDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val barangId: Int,
    val namaSnapshot: String,
    val hargaSatuan: Long,
    val qty: Int,
    val subtotal: Long
)
