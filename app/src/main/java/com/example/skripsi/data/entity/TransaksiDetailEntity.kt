package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaksi_detail",
    indices = [Index("transaksiId")]
)
data class TransaksiDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transaksiId: Int,
    val barangId: Int,
    val namaBarangSnapshot: String,
    val hargaSatuan: Long,
    val qty: Int,
    val subtotal: Long,
    val createdAt: Long
)
