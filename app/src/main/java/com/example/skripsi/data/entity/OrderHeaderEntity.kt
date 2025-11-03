package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_header")
data class OrderHeaderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createdAt: Long,
    val tableId: String? = null,
    val customerName: String? = null,
    val status: String,
    val notes: String? = null,
    val total: Long = 0L
)
