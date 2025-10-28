package com.example.skripsi.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stock_mutation",
    indices = [Index("barangId")]
)
data class StockMutationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val tipe: String,
    val qty: Int,
    val waktu: Long,
    val refType: String? = null,
    val refId: Int? = null
)
