package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi_header")
data class TransaksiHeaderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tanggal: Long,
    val total: Long,
    val metode: String,
    val status: String,
    val customerId: Int? = null
)
