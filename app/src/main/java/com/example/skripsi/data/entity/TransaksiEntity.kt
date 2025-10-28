package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val namaBarang: String,
    val jumlah: Int,
    val totalHarga: Long,
    val tanggal: Long
)
